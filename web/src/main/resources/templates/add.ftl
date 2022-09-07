<html>
<head>
    <script src="/static/js/jquery-3.6.0.min.js"></script>
    <title>
        <#if user.id ?? && user.id != 0>
            修改
        <#else>
            添加
        </#if>
    </title>
</head>
<body>

<form action="/users" method="POST" enctype="multipart/form-data">
    <#if user.id ?? && user.id != 0>
        <input type="hidden" name="id" value="${user.id}"/>
        <input type="hidden" name="_method" value="PUT"/>
    </#if>
    <#--    <#if user.id ?? && user.id != 0>-->
    <img style="width:200px" id="img" src='/picturePreview?id=${user.id}'>
    <#--    </#if>-->
    <input type="file" onchange="pictureImg(this)" name="picture"><br/>
    name: <input type="text" name="name" value="${(user.name)!}">
    sex: <input type="text" name="sex" value="${user.sex}">
    age: <input type="text" name="age" value="${user.age}">
    email: <input type="text" name="email" value="${(user.email)!}">
    时间555: <input type="text" name="birthdate" value="<#if user.birthdate??>${user.birthdate?string('yyyy-MM-dd')}</#if>">

    <input type="submit" value="提交">
</form>
<script type="text/javascript">
    function pictureImg(input) {
        var file = input.files[0];  //获取第一个文件对象 （如果有多张时可使用循环files数组改变多个<img>的 src值）
        if (window.FileReader) {  //判断当前是否支持使用FileReader
            var fr = new FileReader();  //创建读取文件的对象
            fr.readAsDataURL(file); //以读取文件字符串的方式读取文件 但是不能直接读取file,因为文件的内容是存在file对象下面的files数组中的,该方法结束后图片会以data:URL格式的字符串（base64编码）存储在fr对象的result中
            fr.onloadend = function () {
                document.getElementById("img").src = fr.result;
            }
        }
    }
</script>
</body>
</html>