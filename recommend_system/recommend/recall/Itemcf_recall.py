import pymysql
import math
from collections import defaultdict
from datetime import datetime
from . import Itemcf_cache as cache

def build_itemcf_matrix():

    conn = pymysql.connect(
        host="localhost",
        port=3306,
        user="root",
        password="02100796",
        db="shopping_master",
        charset="utf8mb4"
    )

    cursor = conn.cursor()

    cursor.execute("""
        SELECT user_id, product_id, time
        FROM read_history
    """)

    rows = cursor.fetchall()

    user_items = defaultdict(list)

    now = datetime.now()

    for user_id, product_id, created_at in rows:
        days = (
                now - created_at
        ).days

        # time decay
        time_weight = 1 / (1 + days)

        user_items[user_id].append(
            (product_id, time_weight)
        )

    co_matrix = defaultdict(
        lambda: defaultdict(float)
    )

    item_count = defaultdict(int)

    for items in user_items.values():

        if len(items) <= 1:
            continue

        activity_weight = (
                1 / math.log(1 + len(items))
        )

        for item_i, time_weight_i in items:

            item_count[item_i] += 1

            for item_j, time_weight_j in items:

                if item_i == item_j:
                    continue

                # combine weights
                final_weight = (
                        activity_weight
                        *
                        time_weight_i
                        *
                        time_weight_j
                )

                co_matrix[item_i][item_j] += (
                    final_weight
                )

    item_sim_matrix = defaultdict(dict)

    for item_i, related_items in co_matrix.items():

        for item_j, co_count in related_items.items():

            sim_score = (
                    co_count /
                    math.sqrt(
                        item_count[item_i]
                        *
                        item_count[item_j]
                    )
            )

            item_sim_matrix[item_i][item_j] = sim_score

    cursor.close()
    conn.close()

    return item_sim_matrix

def get_itemcf_recall_items(
        behavior_items: list,
        top_k: int = 20
) -> dict:

    # ==================================
    # Empty Behavior
    # ==================================

    if not behavior_items:
        return {}

    # ==================================
    # Build Cache Once
    # ==================================

    if cache.ITEM_SIM_MATRIX is None:
        print("Building ItemCF Matrix...")

        cache.ITEM_SIM_MATRIX = (
            build_itemcf_matrix()
        )

    # ==================================
    # Use Cache
    # ==================================

    item_sim_matrix = cache.ITEM_SIM_MATRIX
    # ==================================
    # Step4:
    # Recall Similar Items
    # ==================================

    recall_scores = defaultdict(float)

    for item in behavior_items:

        if item not in item_sim_matrix:
            continue

        similar_items = sorted(
            item_sim_matrix[item].items(),
            key=lambda x: x[1],
            reverse=True
        )

        for sim_item, sim_score in similar_items[:10]:

            if sim_item in behavior_items:
                continue

            recall_scores[sim_item] += sim_score

    # ==================================
    # Final Rank
    # ==================================

    ranked_items = sorted(
        recall_scores.items(),
        key=lambda x: x[1],
        reverse=True
    )

    return dict(ranked_items[:top_k])