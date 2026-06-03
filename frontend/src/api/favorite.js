import api from './index';

export const favorite = {
    //获取收藏夹列表
    getFavoriteItems(userId) {
        return api.get(`/user/favorite/list/${userId}`);
    },

    //添加商品到收藏夹
    addToFavorite(userId, productId) {
        return api.post('/user/favorite/add', { userId, productId});
    },

    // 删除收藏夹商品
    removeFromFavorite(userId,productId) {
        return api.delete('/user/favorite/remove',{data: {
            userId, productId
        }});
    },

    // 获取收藏夹商品总数
    getFavoriteItemCount() {
        return api.get('/user/favorite/count');
    },

    //获取收藏夹状态
    isFavorite(userId, productId) {
        return api.get('/user/favorite/status', {
            params: {
                userId: userId,
                productId: productId
            }
        });
    }
}
