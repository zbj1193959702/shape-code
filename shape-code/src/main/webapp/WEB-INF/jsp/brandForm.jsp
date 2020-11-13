<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp" %>
<%@include file="/common/path.jsp" %>
<html>
<head>
    <title>客户列表</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" media="screen" href="../../../css/bootstrap.min.css">
    <link rel="stylesheet" href="http://outter-common.toodc.cn/static-source/theme-chalk-index_2.13.0.css"/>
</head>
<body>

<div id="customerInfo">
    <template>
        <el-table align="center" size="mini"
                :data="tableData"
                style="width: 70%;margin-left: 200px;margin-top: 100px;">
            <el-table-column
                    align="center"
                    prop="createTime"
                    label="爬取时间">
            </el-table-column>
            <el-table-column
                    align="center"
                    prop="updateTime"
                    label="更新时间">
            </el-table-column>
            <el-table-column
                    align="center"
                    prop="phone"
                    label="电话">
            </el-table-column>
            <el-table-column align="center"label="状态">
                <template slot-scope="scope">
                    <span v-if="scope.row.status === 1">
                        有效
                    </span>
                    <span v-if="scope.row.status === 0">
                        无效
                    </span>
                </template>
            </el-table-column>
        </el-table>
    </template>
</div>

<jsp:include page="commonJs.jsp"/>

<script>

    let vm = new Vue({
        el: '#customerInfo',
        data: function () {
            return {
                tableData: [],
                total : 0
            };
        },
        created: function () {

        },
        mounted:function() {
            query();
        },
        methods: {

        }
    });

    function query(phone) {
        let query = {
            phone : phone,
            pageStart : 1,
            pageSize : 20
        };
        $.ajax({
            type: 'POST',
            url: _path + '/customer/list',
            data: {
                json : JSON.stringify(query)
            },
            success: function (result) {
                if (result.code === '0') {
                    vm.tableData = result.data.records;
                    vm.total = result.data.totalRecordCount;
                }
            }
        });
    }
</script>
</body>
</html>
