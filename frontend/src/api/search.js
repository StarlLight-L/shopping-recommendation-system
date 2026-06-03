import api from './index';

export const search = {
    //保存搜索历史
    getSearchContent(user_id,keyword){
        return api.post('/search/add',{user_id,keyword})
    },

    //保存用户近100条浏览历史--限制100
    readReadHistory(user_id,product_id){
        return api.post('/search/add/history',{user_id,product_id})
    }
};