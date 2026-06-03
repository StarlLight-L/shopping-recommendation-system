def normalize_scores(item_scores: dict) -> dict:
    if not item_scores:
        return item_scores

    scores = list(item_scores.values())
    min_score = min(scores)
    max_score = max(scores)

    if max_score == min_score:
        # 所有分数相等时，统一设置为 1（或 0.5）
        return {k: 1.0 for k in item_scores}

    return {
        k: (v - min_score) / (max_score - min_score)
        for k, v in item_scores.items()
    }
