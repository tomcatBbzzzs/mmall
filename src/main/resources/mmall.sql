create database `mmall` default charset=utf8;

-- 用户表
create table `mmall_user`(
                             `id` int(11) not null auto_increment comment '用户表id',
                             `username` varchar(50) not null comment '用户名',
                             `password` varchar(50) not null comment '用户密码,MD5加密',
                             `email` varchar(50) default null,
                             `phone` varchar(20) default null,
                             `question` varchar(100) default null comment '找回密码问题',
                             `answer` varchar(100) default null comment '找回密码答案',
                             `role` int(4) not null comment '角色0-管理员,1-普通用户',
                             `create_time`datetime not null comment '创建时间',
                             `update_time` datetime not null comment '最后一次登录时间',
                             primary key (`id`),
    -- 用户名也是唯一的
                             unique key `user_name_unique` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT charset =utf8;


-- 分类表
create table `mmall_category`(
                                 `id` int(11) not null auto_increment comment '类别id',
                                 `parent_id` int(11) default  null comment '夫类别id,为0表示根节点',
                                 `name` varchar(50) default  null comment '类别名称',
                                 `status` tinyint(1) default '1' comment '类别状态-1正常,2-已废弃',
                                 `sort_order` int(4) default null comment '排序编号,同类展示顺序,数值相等则自然排序',
                                 `create_time` datetime default null comment '创建时间',
                                 `update_time` datetime default null comment '更新时间',
                                 primary key (`id`)
) ENGINE =InnoDB AUTO_INCREMENT=100032 default charset =utf8;


-- 产品表
create table `mmall_product`(
                                `id` int(11) not null auto_increment comment '产品id',
                                `category_id` int(11) not null comment '分类id,对应mmall_catrgory表的主键',
                                `name` varchar(100) not null comment '商品名称',
                                `subtitle` varchar(200) default  null comment '商品副标题',
                                `main_image` varchar(500) default null comment '产品主图,对应url相对地址',
                                `sub_images` text comment '图片地址,json格式,扩展用',
                                `detail` text comment '商品详情',
                                `price` decimal(20,2) not null comment '价格,单位-元保留2位小数',
                                `stock` int(11) not null comment '库存数量',
                                `status` int(6) default '1' comment '商品状态,1-在售,2-下架,3-删除',
                                `create_time` datetime default null comment '创建时间',
                                `update_time` datetime default null comment '更新时间',
                                primary key (`id`)
) ENGINE =InnoDB AUTO_INCREMENT=26 default charset =utf8;


-- 购物车表
create table `mmall_cart`(
                             `id` int(11) not null auto_increment comment '购物车id',
                             `user_id` int(11) not null,
                             `product_id` int(11) default null comment '商品id',
                             `quantity` int(11) default null comment '数量',
                             `checked` int(11) default null comment '是否选择, 1=已勾选,0=未勾选',
                             `create_time` datetime default null comment '创建时间',
                             `update_time` datetime default null comment '更新时间',
                             primary key (`id`),
                             KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE =InnoDB AUTO_INCREMENT=121 default charset =utf8;



-- 支付信息表i
create table `mmall_pay_info`(
                                 `id` int(11) not null auto_increment,
                                 `user_id` int(11) default null comment '用户id',
                                 `order_no` bigint(20) default null comment  '订单号',
                                 `pay_platform` int(10) default null comment '支付平台:1-支付宝,2-微信',
                                 `platform_number` varchar(100) default null comment '支付宝流水号',
                                 `platform_status` varchar(20) default null comment '支付宝支付状态',
                                 `create_time` datetime default null comment '创建时间',
                                 `update_time` datetime default null comment '更新时间',
                                 primary key (`id`)
)  ENGINE =InnoDB AUTO_INCREMENT=53 default charset =utf8;



-- 订单表
create table `mmall_order`(
                              `id` int(11) not null auto_increment comment '订单id',
                              `order_no` bigint(20) default null comment '订单号',
                              `user_id` int(11) default null comment '用户id',
                              `shipping_id` int(11) default null comment  '订单id',
                              `payment` decimal(20,2) default null comment '实际付款金额,单位是元,保留2位小数',
                              `payment_type` int(4) default null comment '支付类型,1-在线支付',
                              `postage` int(10) default null comment '运费,单位是元',
                              `status` int(10) default null comment '订单状态:0-已取消,10-未付款,20-已付款,40-已发货,50-已完成,60-交易关闭',
                              `payment_time`  datetime default null comment '支付时间',
                              `send_time` datetime default null comment '发货时间',
                              `end_time` datetime default null comment  '交易完成时间',
                              `close_time` datetime default null comment '交易关闭时间',
                              `create_time` datetime default null comment '创建时间',
                              `update_time` datetime default null comment '更新时间',
                              primary key (`id`),
                              UNIQUE KEY `order_no_index` (`order_no`) USING BTREE
)  ENGINE =InnoDB AUTO_INCREMENT=53 default charset =utf8;


-- 订单明细表
create table `mmall_order_item`(
                                   `id` int(11) not null auto_increment comment '订单子表id',
                                   `user_id` int(11) default null,
                                   `order_no` bigint(20) default null,
                                   `product_id` int(11) default null comment '商品id',
                                   `product_name` varchar(100) default null comment '商品名称',
                                   `product_image` varchar(500) default null comment '商品图片地址',
                                   `current_unit_price` decimal(20,2) default null comment '订单商品单价',
                                   `quantity` int(10) default null comment '商品数量',
                                   `total_price` decimal(20,2) default  null comment '商品总价,单价元-保留2位小数',
                                   `create_time` datetime default null comment '创建时间',
                                   `update_time` datetime default null comment '更新时间',
                                   primary key (`id`),
                                   KEY `order_no_index` (`order_no`) USING BTREE ,
                                   KEY `order_no_user_id_index`(`user_id`, `order_no`) USING BTREE
) ENGINE =InnoDB AUTO_INCREMENT=113 default charset =utf8;



-- 收货地址表
create table `mmall_shipping` (
                                  `id` int(11) not null auto_increment,
                                  `user_id` int(11) default null comment '用户id',
                                  `receiver_name` varchar(20) default null comment '收货姓名',
                                  `receiver_phone` varchar(20) default null comment '收货固定电话',
                                  `receiver_mobile` varchar(20) default  null comment '收货移动电话',
                                  `receiver_province` varchar(20) default null comment '省份',
                                  `receiver_city` varchar(20) default null comment '城市',
                                  `receiver_district` varchar(20) default null comment '区/县',
                                  `receiver_address` varchar(200) default null comment '详细地址',
                                  `receiver_zip` varchar(20) default null comment '邮编',
                                  `create_time` datetime default null comment '创建时间',
                                  `update_time` datetime default null comment '更新时间',
                                  primary key (`id`)
) ENGINE =InnoDB AUTO_INCREMENT=113 default charset =utf8;