let url_prefix = "http://localhost:8080/manage/category";

// 1.获取平类子节点(平级)
let getCategoryRequest = {
    url: url_prefix + "/get_category.do?category=0",
    type: "GET",
    param: {},
    error_response: [
        {
            status: 10,
            msg: "用户未登录,请登录"
        },
        {
            status: 1,
            msg: "未找到该分类"
        },
    ],
    success_response: {
        status: 0,
        data: [
            {
                id: 2,
                parentId: 1,
                name: "手机",
                status: true,
                sortOrder: 3,
                createTime: 1246454154,
                updateTime: 2146534535
            },
            {
                id: 4,
                parentId: 1,
                name: "移动座机",
                status: true,
                sortOrder: 5,
                createTime: 1246454154,
                updateTime: 2146534535
            },
        ]
    }
};


// 2.增加节点
let addCategoryRequest = {
    url: url_prefix + "/add_category.do",
    type: "POST",
    param: {
        parentId: 0,
        categoryName: "衣服"
    },
    error_response: {
        status: 1,
        msg: "添加品类失败"
    },
    success_response: {
        status: 0,
        msg: "添加品类成功"
    }
};

// 3.修改分类名字
let setCategoryNameRequest = {
    url: url_prefix + "/set_category_name.do",
    type: "POST",
    param: {
        parentId: 0,
        categoryName: "衣服"
    },
    error_response: {
        status: 1,
        msg: "更新品类名字失败"
    },
    success_response: {
        status: 0,
        msg: "更新品类名称成功"
    }
};


// 4.获取当前分类和它的子分类的信息
let getDeepCategoryRequest = {
    url: url_prefix + "/get_deep_category.do",
    type: "GET",
    param: {
        categoryId: 1
    },
    error_response: {
        status: 1,
        msg: "无权限"
    },
    success_response: {
        status: 0,
        data: [
            10000,
            10001,
            10002,
            10003
        ]
    }
};


