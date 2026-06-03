import pymysql
import redis
from collections import defaultdict
from datetime import datetime, timedelta
import json

# Redis
redis_client = redis.StrictRedis(host='localhost', port=6379, db=0, decode_responses=True)


def get_user_behavior_items(user_id: str) -> list:
    cached_data = redis_client.get(f"user_behavior:{user_id}")

    if cached_data:
        print("fetch data from Redis")
        return json.loads(cached_data)

    # If the requested data is not available in the cache, fetch it from MySQL
    conn = pymysql.connect(host="localhost", port=3306, user="root", password="02100796", db="shopping_master",
                           charset="utf8mb4")
    cursor = conn.cursor()

    # Set the date to 7 days ago
    date_limit = (datetime.now() - timedelta(days=7)).strftime('%Y-%m-%d')

    item_scores = defaultdict(int)

    # read history -- 1
    cursor.execute("SELECT product_id FROM read_history WHERE user_id=%s", (user_id,))
    browse_items = cursor.fetchall()
    for (item,) in browse_items:
        item_scores[item] += 1

    # favorites -- 3
    cursor.execute("SELECT product_id FROM favorite WHERE user_id=%s AND create_time >= %s", (user_id, date_limit))
    favorite_items = cursor.fetchall()
    for (item,) in favorite_items:
        item_scores[item] += 3

    # cart -- 4
    cursor.execute("SELECT product_id FROM cart_items WHERE user_id=%s AND created_at >= %s", (user_id, date_limit))
    cart_items = cursor.fetchall()
    for (item,) in cart_items:
        item_scores[item] += 4

    # Define the target keywords to be matched, which can be customized according to business needs
    keywords = ["New Arrival", "Best Seller", "Limited Edition", "Recommended", "Flagship"]

    # Order -- 4
    cursor.execute("SELECT order_no FROM `order` WHERE user_id=%s AND create_time >= %s", (user_id, date_limit))
    order_nos = cursor.fetchall()

    for (order_no,) in order_nos:
        cursor.execute("SELECT product_id FROM order_item WHERE order_id=%s", (order_no,))
        order_items = cursor.fetchall()

        for (product_id,) in order_items:
            # basic score
            item_scores[product_id] += 2

            # Retrieve the product's name, description, and tag information
            cursor.execute("SELECT name, description, tags FROM products WHERE id=%s", (product_id,))
            result = cursor.fetchone()
            if result:
                name, description, tags = result
                combined_text = f"{name or ''} {description or ''} {tags or ''}".lower()  # 合并为统一字符串
                if any(kw.lower() in combined_text for kw in keywords):  # 模糊匹配关键词
                    item_scores[product_id] += 2  # 命中关键词，加 2 分

    # search history -- 2
    cursor.execute("SELECT keyword FROM user_search_history WHERE user_id=%s AND searched_at >= %s",
                   (user_id, date_limit))
    keywords = [row[0] for row in cursor.fetchall()]
    for kw in keywords:
        cursor.execute("SELECT id FROM products WHERE name LIKE %s LIMIT 10", ('%' + kw + '%',))
        search_results = cursor.fetchall()
        for (item,) in search_results:
            item_scores[item] += 2

    conn.close()

    # Sort and return the product IDs based on their scores
    ranked = sorted(item_scores.items(), key=lambda x: x[1], reverse=True)
    result = dict(ranked)

    # Cache the result in Redis with an expiration time of 1 hour
    redis_client.setex(
        f"user_behavior:{user_id}",
        3600,
        json.dumps(result)
    )

    return result
