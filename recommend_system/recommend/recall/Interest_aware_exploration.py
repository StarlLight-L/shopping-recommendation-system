import pymysql
import random
from collections import Counter
def get_new_items(limit: int = 20) -> list:

    conn = pymysql.connect(
        host="localhost",
        port=3306,
        user="root",
        password="02100796",
        db="shopping_master",
        charset="utf8mb4"
    )

    cursor = conn.cursor()

    sql = """
        SELECT id
        FROM products
        WHERE JSON_SEARCH(
            tags,
            'one',
            'New Arrival'
        ) IS NOT NULL
    """

    cursor.execute(sql)

    results = cursor.fetchall()

    cursor.close()
    conn.close()

    new_items = [
        row[0]
        for row in results
    ]

    random.shuffle(new_items)

    return new_items[:limit]

def get_interest_exploration_items(user_id: int,
                                   exclude_items: list,
                                   top_k: int = 2) -> list:

    conn = pymysql.connect(
        host="localhost",
        port=3306,
        user="root",
        password="02100796",
        db="shopping_master",
        charset="utf8mb4"
    )

    cursor = conn.cursor()

    # get user browser history

    cursor.execute("""
        SELECT p.category
        FROM read_history r
        JOIN products p
        ON r.product_id = p.id
        WHERE r.user_id = %s
    """, (user_id,))

    browse_categories = [row[0] for row in cursor.fetchall()]

    # if no read record
    # if no read record
    if not browse_categories:

        conn.close()

        all_new_items = get_new_items()

        if not all_new_items:
            return []

        return random.sample(
            all_new_items,
            min(top_k, len(all_new_items))
        )

    # most interested categories

    category_counter = Counter(browse_categories)

    favorite_categories = [
        category
        for category, _
        in category_counter.most_common(2)
    ]

    # explore new arrival in categories

    placeholders = ','.join(['%s'] * len(favorite_categories))

    sql = f"""
        SELECT id
        FROM products
        WHERE category IN ({placeholders})
        AND JSON_UNQUOTE(
            JSON_EXTRACT(tags, '$[0]')
        ) = 'New Arrival'
    """

    cursor.execute(sql, favorite_categories)

    candidate_items = [
        row[0]
        for row in cursor.fetchall()
    ]

    conn.close()
    print("before exclude:", candidate_items)

    candidate_items = list(
        set(candidate_items) - set(exclude_items)
    )

    print("after exclude:", candidate_items)

    # Random exploration
    if len(candidate_items) <= top_k:
        if len(candidate_items) == 0:

            all_new_items = list(
                set(get_new_items()) - set(exclude_items)
            )

            if not all_new_items:
                return []

            return random.sample(
                all_new_items,
                min(top_k, len(all_new_items))
            )
    return random.sample(candidate_items, top_k)