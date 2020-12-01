<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp" %>
<%@include file="/common/path.jsp" %>
<html>
<head>
    <title>旅游</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="referrer" content="no-referrer" />
    <link rel="stylesheet" media="screen" href="../../../css/bootstrap.min.css">
    <link rel="stylesheet" href="http://outter-common.toodc.cn/static-source/theme-chalk-index_2.13.0.css"/>
</head>
<style>
    .el-pagination {
        text-align: center;
        margin-top: 10px;
        margin-bottom: 50px
    }

    .el-input-group>.el-input__inner{
        height: 55px;
    }
</style>
<body>

<div id="house">
    <template>
        <div style="padding-bottom: 50px;padding-top: 50px">
            <el-row style="padding-top: 20px;padding-bottom: 50px">
                <el-col :span="12" :offset="6">
                    <el-card :body-style="{ padding: '0px' }">
                        <el-input style="height: 55px" placeholder="搜索内容" v-model="keyword">
                            <el-button slot="append" icon="el-icon-search"></el-button>
                        </el-input>
                    </el-card>
                </el-col>
            </el-row>
            <el-row :gutter="20" v-for="item in tableData" style="padding-top: 5px;">
                <el-col :span="16" :offset="4">
                    <el-card class="box-card">
                        <el-container>
                            <el-aside width="200px">
                                <img width="200px" height="160px" :src="item.firstImage"/>
                            </el-aside>
                            <el-container>
                                <el-header style="height: 15px">
                                    <a @click="showDetail(item.detailUrl)">
                                        <span style="font-size: 20px;font-weight: bolder;">{{item.title}}</span>
                                    </a>
                                </el-header>
                                <el-main>
                                        <el-col :offset="18" :span="6">
                                           <span><span style="color: rgb(242 116 96); font-size: 23px"> {{item.price}} </span>元/平米</span>
                                        </el-col>
                                        <el-col :span="20">
                                            {{item.norms}}
                                        </el-col>
                                </el-main>
                            </el-container>
                        </el-container>
                    </el-card>
                </el-col>
            </el-row>
            <el-pagination
                    class="fy"
                    :page-size="pageSize"
                    @current-change="currentChange"
                    layout="total,prev, pager, next,jumper"
                    :total="total"
                    :current-page="currentPage"
                    background>
            </el-pagination>
        </div>
    </template>
</div>

<jsp:include page="commonJs.jsp"/>

<script>

    var vm = new Vue({
        el: '#house',
        data: function () {
            return {
                keyword: null,
                tableData: [],
                pageSize: 20,
                currentPage: 1,
                total: 0,
            };
        },
        mounted:function() {
            this.queryList(1);
        },
        methods: {
            showDetail(url) {
                window.open(url);
            },
            currentChange: function (currentPage) {
                vm.currentPage = currentPage;
                this.queryList(currentPage);
            },
            queryList(pageNumber) {
                let query = {
                    pageStart : pageNumber,
                    pageSize : 20,
                };
                $.ajax({
                    type: 'POST',
                    url: _path + '/house/list',
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
        }
    });

</script>
</body>
</html>
