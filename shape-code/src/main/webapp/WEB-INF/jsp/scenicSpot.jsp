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
    <link rel="stylesheet" media="screen" href="../../../css/bootstrap.min.css">
    <link rel="stylesheet" href="http://outter-common.toodc.cn/static-source/theme-chalk-index_2.13.0.css"/>
</head>
<style>
    .el-pagination {
        text-align: center;
        margin-bottom: 50px
    }
</style>
<body>

<div id="scenicSpot">
    <template>
        <el-form style="margin-left: 50px;margin-top: 50px;" :inline="true" >
            <el-form-item label="标题">
                <el-input v-model="query.title" placeholder="标题"></el-input>
            </el-form-item>
            <el-form-item label="推荐理由">
                <el-input v-model="query.common" placeholder="推荐理由"></el-input>
            </el-form-item>
            <el-form-item label="最高价格">
                <el-input v-model="query.maxPrice" placeholder="最高价格"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="queryList(1)">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="text">{{time}}</el-button>
                <el-button type="primary" @click="startWorker()">开始</el-button>
                <el-button type="primary" @click="stopTime()">停止</el-button>
            </el-form-item>
        </el-form>
        <el-table align="center" size="small"
                :data="tableData" border
                style="width: 90%;margin-left: 50px;">
            <el-table-column align="center"label="状态">
                <template slot-scope="scope">
                    <img width="230px" height="160px" :src="scope.row.photoUrl"/>
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    label="标题">
            </el-table-column>
            <el-table-column
                    align="center"
                    label="价格">
                <template slot-scope="scope">
                    <span>{{scope.row.price}}  元</span>
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    prop="common"
                    label="推荐理由">
            </el-table-column>
            <el-table-column
                    align="center"
                    prop="createTime"
                    label="创建时间">
            </el-table-column>
        </el-table>
        <el-pagination
                class="fy"
                :page-size="pageSize"
                @size-change="handleSizeChange"
                @current-change="currentChange"
                layout="total,prev, pager, next,jumper"
                :total="total"
                :current-page="currentPage"
                background>
        </el-pagination>
    </template>
</div>

<jsp:include page="commonJs.jsp"/>

<script>

    var vm = new Vue({
        el: '#scenicSpot',
        data: function () {
            return {
                pageSize: 20,
                totalPage: 0,
                currentPage: 1,
                tableData: [],
                total : 0,
                query: {
                    title: null,
                    common: null,
                    maxPrice: null,
                },
                time : null,
            };
        },
        created: function () {

        },
        mounted:function() {
            this.queryList(1);
        },
        methods: {
            startWorker() {
              startWorker();
            },
            stopTime(){
                stopTime();
            },
            handleSizeChange() {
            },
            currentChange: function (currentPage) {
                vm.currentPage = currentPage;
                this.queryList(currentPage);
            },
            queryList(pageNumber) {
                let query = {
                    pageStart : pageNumber,
                    pageSize : this.pageSize,
                    title: this.query.title,
                    common: this.query.common,
                    maxPrice: this.query.maxPrice,
                };
                $.ajax({
                    type: 'POST',
                    url: _path + '/scenicSpot/list',
                    data: {
                        json : JSON.stringify(query)
                    },
                    success: function (result) {
                        if (result.code === '0') {
                            vm.tableData = result.data.records;
                            vm.total = result.data.totalRecordCount;
                            setTimeout(function () {
                                // 表格内容被el-table转义
                                for (let i = 0; i < vm.tableData.length; i++) {
                                    let selector = '#scenicSpot > div.el-table.el-table--fit.el-table--border.el-table--enable-row-hover.el-table--enable-row-transition.el-table--small > div.el-table__body-wrapper.is-scrolling-none > table > tbody > tr:nth-child('+
                                        (i+1) +') > td.el-table_1_column_2.is-center > div';
                                    $(selector).html('');
                                    $(selector).append(vm.tableData[i].title)
                                }
                            }, 300)
                        }
                    }
                });
            }
        }
    });

    var worker;

    function startWorker() {
        if(typeof(Worker)!=="undefined") {
            if(typeof(worker) == "undefined") {
                worker = new Worker("../../../js/workerTime.js");
            }
            worker.onmessage = function (event) {
                vm.time = event.data;
            };
        } else {
            vm.time = "Sorry, your browser does not support Web Workers...";
        }
    }

    function stopTime() {
        if (worker !== undefined) {
            worker.terminate();
        }
    }

</script>
</body>
</html>
