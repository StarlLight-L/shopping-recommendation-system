import pandas as pd
import torch
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder, MinMaxScaler
import torch.nn as nn
from torch.utils.data import DataLoader
import torch.optim as optim
import numpy as np
import random
import joblib

from deepfm_model import deepfm

if __name__ == "__main__":
    def get_auc(loader, model):

        pred = []
        target = []

        model.eval()

        with torch.no_grad():
            for x, y in loader:
                x = x.float()
                y = y.float()

                y_hat = model(x)

                pred += list(
                    y_hat.detach()
                        .cpu()
                        .numpy()
                        .flatten()
                )

                target += list(
                    y.detach()
                        .cpu()
                        .numpy()
                        .flatten()
                )

        pred = np.array(pred)
        target = np.array(target)

        desc_score_indices = np.argsort(pred)[::-1]

        target_sorted = target[
            desc_score_indices
        ]

        npos = target_sorted.sum()
        nneg = len(target_sorted) - npos

        tp = np.cumsum(target_sorted)
        fp = np.cumsum(1 - target_sorted)

        tpr = tp / npos
        fpr = fp / nneg

        auc = np.trapz(tpr, fpr)

        return auc
    batch_size = 1024
    lr = 0.0005
    wd = 0.0001
    epoches = 3

    seed = 1024

    torch.manual_seed(seed)
    torch.cuda.manual_seed(seed)
    torch.cuda.manual_seed_all(seed)

    np.random.seed(seed)
    random.seed(seed)

    sparse_features = [
        'user_id',
        'product_id',
        'category_id',
        'brand',
        'device'
    ]

    dense_features = [
        'price',
        'click_count',
        'favorite_count',
        'cart_count',
        'purchase_count'
    ]

    feature_names = (
        sparse_features +
        dense_features
    )

    # ======================
    # Load Data
    # ======================

    df = pd.read_csv('training_data.csv')

    # ======================
    # Fill Missing
    # ======================

    df[sparse_features] = (
        df[sparse_features]
        .fillna('-1')
    )

    df[dense_features] = (
        df[dense_features]
        .fillna(0)
    )

    # ======================
    # Label Encoder
    # ======================

    label_encoders = {}

    for feat in sparse_features:

        df[feat] = df[feat].astype(str)

        lbe = LabelEncoder()

        df[feat] = lbe.fit_transform(
            df[feat]
        )

        label_encoders[feat] = lbe

    # ======================
    # Save Encoder
    # ======================

    joblib.dump(
        label_encoders,
        'label_encoders.pkl'
    )

    # ======================
    # MinMaxScaler
    # ======================

    mms = MinMaxScaler(
        feature_range=(0, 1)
    )

    df[dense_features] = (
        mms.fit_transform(
            df[dense_features]
        )
    )

    # ======================
    # Save Scaler
    # ======================

    joblib.dump(
        mms,
        'scaler.pkl'
    )

    # ======================
    # Feature Size
    # ======================

    feat_size1 = {
        feat: 1
        for feat in dense_features
    }

    feat_size2 = {
        feat: len(
            label_encoders[feat].classes_
        )
        for feat in sparse_features
    }

    feat_sizes = {}

    feat_sizes.update(feat_size2)
    feat_sizes.update(feat_size1)

    # ======================
    # Train Test Split
    # ======================

    train, test = train_test_split(
        df,
        test_size=0.2,
        random_state=2021
    )

    # ======================
    # Tensor Dataset
    # ======================

    train_label = train[['label']]
    train_data = train[feature_names]

    test_label = test[['label']]
    test_data = test[feature_names]

    train_tensor_data = torch.utils.data.TensorDataset(
        torch.from_numpy(
            np.array(train_data)
        ),
        torch.from_numpy(
            np.array(train_label)
        )
    )

    test_tensor_data = torch.utils.data.TensorDataset(
        torch.from_numpy(
            np.array(test_data)
        ),
        torch.from_numpy(
            np.array(test_label)
        )
    )

    train_loader = DataLoader(
        dataset=train_tensor_data,
        shuffle=True,
        batch_size=batch_size
    )

    test_loader = DataLoader(
        dataset=test_tensor_data,
        shuffle=False,
        batch_size=batch_size
    )

    # ======================
    # Model
    # ======================

    device = 'cpu'

    model = deepfm(
        feat_sizes,
        sparse_feature_columns=sparse_features,
        dense_feature_columns=dense_features,
        dnn_hidden_units=[256, 128],
        dnn_dropout=0.5,
        ebedding_size=16,
        l2_reg_linear=1e-3,
        device=device
    )

    # ======================
    # Loss & Optimizer
    # ======================

    loss_func = nn.BCELoss(
        reduction='mean'
    )

    optimizer = optim.Adam(
        model.parameters(),
        lr=lr,
        weight_decay=wd
    )

    # ======================
    # Train
    # ======================

    for epoch in range(epoches):

        total_loss_epoch = 0.0
        total_tmp = 0

        model.train()

        for x, y in train_loader:
            x = x.float()
            y = y.float()

            y_hat = model(x)

            optimizer.zero_grad()

            loss = loss_func(
                y_hat,
                y
            )

            loss.backward()

            optimizer.step()

            total_loss_epoch += loss.item()
            total_tmp += 1

        auc = get_auc(
            test_loader,
            model
        )

        print(
            'epoch/epoches: {}/{}, train loss: {:.3f}, test auc: {:.3f}'
                .format(
                epoch,
                epoches,
                total_loss_epoch / total_tmp,
                auc
            )
        )

    # ======================
    # Save Model
    # ======================

    torch.save(
        model.state_dict(),
        'deepfm.pth'
    )

    print("训练完成")