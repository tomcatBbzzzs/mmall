let url_prefix = "http://localhost:8080/user";

// 1.登录请求
let loginRequest = {
    "url": url_prefix + "/login.do",
    "type": "POST",
    "param": {
        "username": "admin",
        "password": 1234
    },
    "error_response": {
        "status": 1,
        "msg": "密码错误"
    },
    "success_response": {
        "status": 0,
        "data": {
            "id": 12,
            "username": "aaa",
            "email": "aaa@163.com",
            "phone": null,
            "role": 0,
            "createTime": 1246454154,
            "updateTime": 2146534535
        }
    }
};


// 2.注册请求
let registerRequest = {
    "url": url_prefix + "/register.do",
    "type": "POST",
    "param": {
        "username": "admin",
        "password": 1234,
        "email": "123234558@163.com",
        "phone": "12545245164",
        "question": "问题",
        "answer": "答案"
    },
    "error_response": {
        "status": 1,
        "msg": "用户已存在"
    },
    "success_response": {
        "status": 0,
        "msg": "效验成功"
    }
};


// 3.检查用户名是否有效
let checkValidRequest = {
    "url": url_prefix + "/register.do?sty=admin&type=username",
    "type": "GET",
    "param": {},
    "error_response": {
        "status": 1,
        "msg": "用户已存在"
    },
    "success_response": {
        "status": 0,
        "msg": "效验成功"
    }
};


// 4.获取登录用户的信息
let userInfoRequest = {
    "url": url_prefix + "/get_user_info.do",
    "type": "GET",
    "param": {},
    "error_response": {
        "status": 1,
        "msg": "用户未登录,无法获取用户信息"
    },
    "success_response": {
        "status": 0,
        "data": {
            "id": 12,
            "username": "aaa",
            "email": "aaa@163.com",
            "phone": null,
            "role": 0,
            "createTime": 1246454154,
            "updateTime": 2146534535
        }
    }
};


// 5.忘记密码
let forgetGetQuestionRequest = {
    "url": url_prefix + "/forget_check_answer.do?username=geely",
    "type": "GET",
    "param": {},
    "error_response": {
        "status": 1,
        "msg": "用户没有设置找回密码问题"
    },
    "success_response": {
        "status": 0,
        "data": "这里是问题"
    }
};


// 6.提交问题答案
let forGetCheckAnswerRequest = {
    "url": url_prefix + "/forget_check_answer.do?username=geely&question=aa&answer=sss",
    "type": "GET",
    "param": {},
    "error_response": {
        "status": 1,
        "msg": "问题密码错误"
    },
    "success_response": {
        "status": 0,
        "data": "531ef4b4-9663-4e6d-9a20-fb56367446a5"
    }
};


// 7.重设密码
let forgetResetPasswordRequest = {
    "url": url_prefix + "/for_reset_password.do?username=aaa&password=newPassword&Token=531ef4b4-9663-4e6d-9a20-fb56367446a5",
    "type": "GET",
    "param": {},
    "success_response": {
        "status": 0,
        "msg": "密码修改成功"
    },
    "error_response": {
        "status": 1,
        "msg": "密码修改失败 或 token已失效"
    }
};


// 8.登录情况下重置密码
let resrtPasswordRequest = {
    "url": url_prefix + "/reset_password.do",
    "type": "POST",
    "param": {
        "password": "1234",
        "passwordNew": "1234523"
    },
    "success_response": {
        "status": 0,
        "msg": "密码修改成功"
    },
    "error_response": {
        "status": 1,
        "msg": "旧密码输入错误"
    }
};


// 9.登录状态修改个人信息
let updateInfoRequest = {
    "url": url_prefix + "/update_info.do",
    "type": "POST",
    "param": {
        "email": "newEmail",
        "phone": "newPhone"
    },
    "success_response": {
        "status": 0,
        "msg": "更新个人信息成功"
    },
    "error_response": {
        "status": 1,
        "msg": "用户未登录"
    }
};


// 10.获取当前的用户的信息,并强制登录
let getInfoRequest = {
    "url": url_prefix + "/get_info.do",
    "type": "GET",
    "param": {},
    "success_response": {
        "status": 0,
        "data": {
            "id": 12,
            "username": "aaa",
            "email": "aaa@163.com",
            "phone": null,
            "role": 0,
            "createTime": 1246454154,
            "updateTime": 2146534535
        }
    },
    "error_response": {
        "status": 10,
        "msg": "用户未登录, 即将跳转登录"
    }
};


// 11.退出登录的接口
let logoutRequest = {
    "url": url_prefix + "/logout.do",
    "type": "GET",
    "param": {},
    "success_response": {
        "status": 0,
        "msg": "退出成功"
    },
    "error_response": {
        "status": 1,
        "msg": "服务器异常"
    }
};


let url_admin_prefix = "http://localhost:8080/manage/user";


// 12.管理员登录
let adminLoginRequest = {
    url: url_admin_prefix + "/login.do",
    type: "POST",
    param: {
        username: "用户名",
        password: "密码"
    },
    success_response: {
        status: 0,
        data: {
            id: 12,
            username: "aaa",
            email: "aaa@163.com",
            phone: null,
            role: 0,
            createTime: 1246454154,
            updateTime: 2146534535
        }
    },
    error_response: {
        status: 1,
        msg: "密码错误"
    }
};
