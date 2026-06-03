import pymysql
from collections import defaultdict
import json

def get_similarity_recall_items(
        behavior_items: list,
        top_k: int = 20
) -> dict:

    conn = pymysql.connect(
        host="localhost",
        port=3306,
        user="root",
        password="02100796",
        db="shopping_master",
        charset="utf8mb4"
    )

    cursor = conn.cursor()

    similarity_scores = defaultdict(float)
    # =========================
    # User Preference
    # =========================

    category_preference = defaultdict(int)
    brand_preference = defaultdict(int)

    for product_id in behavior_items[:10]:

        cursor.execute("""
            SELECT category, tags
            FROM products
            WHERE id = %s
        """, (product_id,))

        result = cursor.fetchone()

        if not result:
            continue

        category, tags = result

        if isinstance(tags, str):
            tags = json.loads(tags)

        brand = tags[1] if len(tags) > 1 else "Unknown"

        category_preference[category] += 1
        brand_preference[brand] += 1

    # extend user product

    for product_id in behavior_items[:10]:

        # get product information
        cursor.execute("""
            SELECT category, tags, price
            FROM products
            WHERE id = %s
        """, (product_id,))

        result = cursor.fetchone()

        if not result:
            continue

        category, tags, price = result

        if isinstance(tags, str):
            tags = json.loads(tags)

        brand = tags[1] if len(tags) > 1 else None

        # similar brand

        cursor.execute("""
            SELECT id
            FROM products
            WHERE category = %s
            AND id != %s
            LIMIT 20
        """, (category, product_id))

        category_items = cursor.fetchall()

        category_weight = (
                1 +
                category_preference[category] * 0.5
        )

        for (item_id,) in category_items:
            similarity_scores[item_id] += (
                category_weight
            )

        # similar brand

        cursor.execute("""
            SELECT id
            FROM products
            WHERE JSON_UNQUOTE(JSON_EXTRACT(tags, '$[1]')) = %s
            AND id != %s
            LIMIT 10
        """, (brand, product_id))

        brand_items = cursor.fetchall()

        brand_weight = (
                1 +
                brand_preference[brand] * 0.3
        )

        for (item_id,) in brand_items:
            similarity_scores[item_id] += (
                brand_weight
            )

        # similar price product

        cursor.execute("""
            SELECT id, price
            FROM products
            WHERE id != %s
            LIMIT 30
        """, (product_id,))

        price_items = cursor.fetchall()

        for item_id, item_price in price_items:
            price = float(price)
            item_price = float(item_price)

            distance = abs(price - item_price)

            price_score = 1 / (1 + distance / 1000)

            similarity_scores[item_id] += (
                    price_score * 2
            )

    conn.close()

    for item in behavior_items:
        similarity_scores.pop(item, None)

    # rank

    ranked_items = sorted(
        similarity_scores.items(),
        key=lambda x: x[1],
        reverse=True
    )

    return dict(ranked_items[:top_k])