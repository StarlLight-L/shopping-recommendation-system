import pymysql
import random
from .recall.Behavior_recall import get_user_behavior_items
from .device import get_user_device_info, apply_negative_feedback, apply_positive_feedback
from .normalization import normalize_scores
from .recall.Interest_aware_exploration import get_interest_exploration_items
from .recall.similarity_recall import get_similarity_recall_items

from .recall.Itemcf_recall import get_itemcf_recall_items

import json


def get_new_items() -> list:
    conn = pymysql.connect(host="localhost", port=3306, user="root", password="02100796",
                           db="shopping_master", charset="utf8mb4")
    cursor = conn.cursor()
    cursor.execute("SELECT id FROM products WHERE JSON_UNQUOTE(JSON_EXTRACT(tags, '$[0]')) = 'New Arrival';")
    results = cursor.fetchall()
    cursor.close()
    conn.close()

    new_items = [row[0] for row in results]
    return new_items


def recommend_for_user(user_id: int,predictor) -> list:
    # Retrieve the initial recommendation list based on user behavior scores (returns sorted item IDs)
    behavior_scores = get_user_behavior_items(user_id)

    similarity_items = get_similarity_recall_items(
        list(behavior_scores.keys())
    )

    item_scores = {}

    # behavior recall
    for item, score in behavior_scores.items():
        item_scores[item] = score

    # similarity recall
    for item, score in similarity_items.items():
        item_scores[item] = (
                item_scores.get(item, 0)
                + score
        )

    # itemcf recall
    itemcf_items = get_itemcf_recall_items(
        list(behavior_scores.keys())
    )
    for item, score in itemcf_items.items():
        item_scores[item] = (
                item_scores.get(item, 0)
                + score * 2
        )

    # Adjust the score based on device information
    device_info = get_user_device_info(user_id)
    device_type = device_info.get("device_type") or ""
    device_model = device_info.get("device_model") or ""

    if device_type:
        item_scores = apply_negative_feedback(item_scores, device_type)
    if device_model:
        item_scores = apply_positive_feedback(item_scores, device_model.lower().split())

    # Sort recommendations and filter items with positive scores
    ranked_items = sorted(item_scores.items(), key=lambda x: x[1], reverse=True)
    recommended_ids = [item_id for item_id, score in ranked_items if score > 0]

    # =========================
    # DeepFM Ranking
    # =========================

    candidate_products = []

    conn = pymysql.connect(
        host="localhost",
        port=3306,
        user="root",
        password="02100796",
        db="shopping_master",
        charset="utf8mb4"
    )

    cursor = conn.cursor(pymysql.cursors.DictCursor)

    for item_id in recommended_ids:

        cursor.execute("""
            SELECT
                id,
                category,
                tags,
                price
            FROM products
            WHERE id = %s
        """, (item_id,))

        product = cursor.fetchone()
        if not product:
            continue
        tags = json.loads(product['tags'])
        brand = 'Unknown'
        BRANDS = [
            'Apple',
            'Samsung',
            'Huawei',
            'Xiaomi',
            'Sony',
            'ASUS',
            'HP',
            'Lenovo',
            'Dell',
            'OPPO',
            'vivo',
            'MECHREVO',
            'iQOO'
        ]
        for tag in tags:

            if tag in BRANDS:
                brand = tag
                break

        cursor.execute("""
            SELECT COUNT(*) AS cnt
            FROM read_history
            WHERE user_id = %s
            AND product_id = %s
        """, (user_id, item_id))

        click_count = cursor.fetchone()['cnt']

        cursor.execute("""
            SELECT COUNT(*) AS cnt
            FROM favorite
            WHERE user_id = %s
            AND product_id = %s
        """, (user_id, item_id))

        favorite_count = cursor.fetchone()['cnt']

        cursor.execute("""
            SELECT COUNT(*) AS cnt
            FROM cart_items
            WHERE user_id = %s
            AND product_id = %s
        """, (user_id, item_id))

        cart_count = cursor.fetchone()['cnt']

        cursor.execute("""
            SELECT COUNT(*) AS cnt
            FROM order_item
            WHERE product_id = %s
        """, (item_id,))

        purchase_count = cursor.fetchone()['cnt']


        if not product:
            continue

        sample = {

            'user_id': user_id,

            'product_id': product['id'],

            'category_id': product['category'].capitalize(),

            'brand': brand,

            'device': device_type,

            'price': float(product['price']),

            'click_count': click_count,

            'favorite_count': favorite_count,

            'cart_count': cart_count,

            'purchase_count': purchase_count
        }

        candidate_products.append(sample)

    cursor.close()
    conn.close()
    #print("candidate_products:", len(candidate_products))
    #print(candidate_products[:3])
    # DeepFM rank
    ranked_result = predictor.rank(candidate_products)
    #print("ranked_result:", len(ranked_result))
    # TopK
    main_recommendations = [
        item['product_id']
        for item in ranked_result[:6]
    ]

    # fetch new items
    new_items = get_new_items()

    # Remove products already present in the main recommendation list
    remaining_new_items = list(set(new_items) - set(main_recommendations))

    # If the main recommendation list contains fewer than 6 items, fill the remaining slots with new products
    needed = 6 - len(main_recommendations)
    if needed > 0:
        supplement = random.sample(remaining_new_items, min(needed, len(remaining_new_items)))
        main_recommendations += supplement
        remaining_new_items = list(set(remaining_new_items) - set(supplement))  # 避免重复

    # interest exploration items
    exploration_items = get_interest_exploration_items(
        user_id=user_id,
        exclude_items=main_recommendations,
        top_k=2
    )

    #exploration_items = [1, 2]

    final_recommendations = (main_recommendations + exploration_items)[:8]

    # normalization
    #item_scores = normalize_scores(item_scores)
    #print(item_scores)
    return final_recommendations

