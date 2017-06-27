### 一些事项
* 数据库表字段使用下划线分割,Java POJO使用驼峰法.注意设置MyBatis配置项
* url全部小写
    * 前台url /模块/[资源]/方法/[参数名]/[参数值]
        * list方法使用?page=[page]&pageSize=[pageSize]&sort=[sort]
    * 后台url /admin/模块/[资源]/方法/[参数名]/[参数值] /admin/buy/file/parse/filename/xxxxx.xyz
* 前端
    * class使用-分割
    * js变量使用驼峰法,使用单引号
* ajax返回格式

    返回成功:

        [
            "success":true,
            "data":[
                {id:"1",create_date:"2010-01-01 11:11:11"},
                {id:"2",create_date:"2010-01-01 11:11:11"},
                {id:"3",create_date:"2010-01-01 11:11:11"}
            ]
        ]

    返回失败

        [       
            "success":false,
            "message":"权限不够"
        ]
