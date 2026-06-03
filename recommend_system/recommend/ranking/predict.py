import torch
import pandas as pd
import numpy as np
import joblib
import os
from .deepfm_model import deepfm


class DeepFMPredictor:

    def __init__(self):

        # ======================
        # Feature Definition
        # ======================

        self.sparse_features = [
            'user_id',
            'product_id',
            'category_id',
            'brand',
            'device'
        ]

        self.dense_features = [
            'price',
            'click_count',
            'favorite_count',
            'cart_count',
            'purchase_count'
        ]

        self.feature_names = (
                self.sparse_features +
                self.dense_features
        )

        BASE_DIR = os.path.dirname(
            os.path.abspath(__file__)
        )

        self.label_encoders = joblib.load(
            os.path.join(BASE_DIR, 'label_encoders.pkl')
        )

        self.mms = joblib.load(
            os.path.join(BASE_DIR, 'scaler.pkl')
        )

        # ======================
        # Feature Size
        # ======================

        feat_size1 = {
            feat: 1
            for feat in self.dense_features
        }

        feat_size2 = {
            feat: len(
                self.label_encoders[feat].classes_
            )
            for feat in self.sparse_features
        }
        self.feat_sizes = {}

        self.feat_sizes.update(feat_size2)
        self.feat_sizes.update(feat_size1)

        # ======================
        # Device
        # ======================

        self.device = 'cpu'

        # ======================
        # Load Model
        # ======================

        self.model = deepfm(
            self.feat_sizes,
            sparse_feature_columns=self.sparse_features,
            dense_feature_columns=self.dense_features,
            dnn_hidden_units=[256, 128],
            dnn_dropout=0.5,
            ebedding_size=16,
            l2_reg_linear=1e-3,
            device=self.device
        )

        self.model.load_state_dict(
            torch.load(
                './recommend/ranking/deepfm.pth',
                weights_only=True
            )
        )

        self.model.eval()

    # ==================================
    # Feature Process
    # ==================================

    def process_features(self, sample):

        sample = sample.copy()

        # sparse feature encoder
        for feat in self.sparse_features:

            sample[feat] = str(sample[feat])

            if sample[feat] in self.label_encoders[feat].classes_:

                sample[feat] = (
                    self.label_encoders[feat]
                    .transform([sample[feat]])[0]
                )

            else:
                sample[feat] = 0

        # dataframe
        df = pd.DataFrame([sample])

        # dense normalize
        df[self.dense_features] = (
            self.mms.transform(
                df[self.dense_features]
            )
        )

        return df

    # ==================================
    # Predict Single Item
    # ==================================

    def predict(self, sample):

        df = self.process_features(sample)

        x = torch.tensor(
            df[self.feature_names].values,
            dtype=torch.float32
        )

        with torch.no_grad():

            pred = self.model(x)

        return float(pred.item())

    # ==================================
    # Rank Candidate Products
    # ==================================

    def rank(self, candidate_products):

        result = []

        for product in candidate_products:

            score = self.predict(product)

            result.append({
                'product_id': product['product_id'],
                'score': score
            })

        result = sorted(
            result,
            key=lambda x: x['score'],
            reverse=True
        )

        return result