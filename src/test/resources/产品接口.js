let url_prefix = "http://localhost:8080/product/";

// 1.产品搜索以及动态排序List
let listRequest = {
    url: url_prefix + "list.do",
    type: "GET",
    param: {
        categoryId: 0,
        keyword: "关键字",
        pageNum: 1,
        pageSize: 10,
        orderBy: [
            'price_desc',
            'price_asc'
        ]
    },
    error_response: {
        status: 1,
        msg: "当前用户未登录"
    },
    success_response: {
        status: 0,
        data: {
            pageNum: 1,
            pageSize: 10,
            size: 2,
            orderBy: null,
            startRow: 1,
            endRow: 2,
            total: 2,
            list: [
                {
                    id: 1,
                    categoryId: 3,
                    name: "iphone7",
                    subtitle: "双十一促销",
                    mainImage: "mainImage.jpeg",
                    price: 6999
                },
                {
                    id: 1,
                    categoryId: 3,
                    name: "iphone8",
                    subtitle: "双十一促销",
                    mainImage: "mainImage.jpeg",
                    price: 7999
                }
            ]
        }
    }
};


// 2.产品detail页
let detailRequest = {
    url: url_prefix + "detail.do?productId=2",
    type: "GET",
    param: {},
    success_response: {
        status: 0,
        data: {
            id: 10002,
            categoryId: 2,
            name: "小米10",
            subtitle: "小米10促销进行中",
            mainImage: "mainImage.png",
            subImages: [
                "mmall/aa.jpg",
                "mmall/bb.jpg",
                "mmall/cc.jpg"
            ]
        }
    }
};


let url_manage_prefix = "http://localhost:8080/manage/product/";


// 3.后台产品接口
let listRequest = {
    url: url_manage_prefix + "list.do",
    type: "GET",
    param: {
        pageNum: 1,
        pageSize: 10
    },
    error_response: {
        status: 1,
        msg: "当前用户未登录"
    },
    success_response: {
        status: 0,
        data: {
            pageNum: 1,
            pageSize: 10,
            size: 2,
            orderBy: null,
            startRow: 1,
            endRow: 2,
            total: 2,
            list: [
                {
                    id: 1,
                    categoryId: 3,
                    name: "iphone7",
                    subtitle: "双十一促销",
                    mainImage: "mainImage.jpeg",
                    price: 6999
                },
                {
                    id: 1,
                    categoryId: 3,
                    name: "iphone8",
                    subtitle: "双十一促销",
                    mainImage: "mainImage.jpeg",
                    price: 7999
                }
            ]
        }
    }
};


let upForm = `
    <form name="form2" action="/manage/product/upload.do" method="POST" enctype="multipart/form-data">
        <input type="file" name="file" />
        <input type="submit" name="upload" />
    </form>
`;

// 4.图片上传
let uploadRequest = {
    url: url_manage_prefix + "upload.do",
    type: "POST",
    param: {
        form: upForm
    },
    success_response: {
        status: 0,
        data: {
            uri: "UUID - System.currentMillis().jpg",
            url: `http://img.bbzzzs.com/${this.uri}.jpg`,
            console: function() {
                console.log(this.uri);
            }
        }
    }
};


// 4.富文本上传图片
let richtextImgUploadRequest = {
    url: url_manage_prefix + "richtext_img_upload_.do",
    type: "POST",
    param: {
        form: upForm
    },
    success_response: {
        success: true,
        msg: "上传成功",
        file_path: "http://img.happymmall.com/UUID-System.currentMillis().jpg"
    },
    error_response: {
        success: false,
        msg: "上传错误",
        file_path: "[real file path]"
    }
};