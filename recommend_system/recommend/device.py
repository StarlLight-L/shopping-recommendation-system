import pymysql

# 获取用户设备信息
def get_user_device_info(user_id: int) -> dict:
    conn = pymysql.connect(host="localhost", port=3306, user="root", password="02100796", db="shopping_master",charset="utf8mb4")# 数据库连接信息

    cursor = conn.cursor()
    cursor.execute("SELECT device_type, device_model FROM devices WHERE user_id=%s", (user_id,))
    result = cursor.fetchone()
    conn.close()
    if result:
        return {"device_type": result[0], "device_model": result[1]}
    return {"device_type": None, "device_model": None}


# 修改 item_scores 的值：给同类型商品减分
def apply_negative_feedback(item_scores: dict, device_type: str) -> dict:
    conn = pymysql.connect(host="localhost", port=3306, user="root", password="02100796", db="shopping_master",charset="utf8mb4")# 数据库连接信息
    cursor = conn.cursor()
    for id in item_scores:
        cursor.execute("SELECT category FROM products WHERE id=%s", (id,))
        result = cursor.fetchone()
        if result and result[0] == device_type:  # category 等于 device_type
            item_scores[id] -= 3  # 给予负反馈

    conn.close()
    return item_scores

# 给同品牌产品加分 2
# 品牌 + 设备型号（模糊匹配）正反馈
def apply_positive_feedback(item_scores: dict, device_model: str) -> dict:
    conn = pymysql.connect(
        host="localhost",
        port=3306,
        user="root",
        password="02100796",
        db="shopping_master",
        charset="utf8mb4"
    )
    cursor = conn.cursor()
    for id in item_scores:
        cursor.execute("SELECT name FROM products WHERE id=%s", (id,))
        result = cursor.fetchone()
        if result:
            name = result[0].lower()  # 商品名称转小写
            if any(kw in name for kw in device_model):
                item_scores[id] += 2  # 模糊匹配成功，加 2 分



    conn.close()
    return item_scores
