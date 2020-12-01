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

    .node circle {
        fill: yellow;
        stroke: red;
        stroke-width: 1.5px;
    }

    .node {
        font: 13px sans-serif;
    }

    .link {
        fill: none;
        stroke: rgba(48, 196, 200, 0.75);
        stroke-width: 1.5px;
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
        </el-form>
        <el-form style="margin-left: 50px;" :inline="true" >
            <el-form-item label="区域">
                <el-col :span="8">
                    <el-select v-model="query.provinceCode" placeholder="省" @change="selProvince">
                        <el-option v-for="item in provinceList" :label="item.name" :value="item.regionCode" :key="item.regionCode"></el-option>
                    </el-select>
                </el-col>
                <el-col :span="8">
                    <el-select v-model="query.cityCode" placeholder="市" @change="selCity">
                        <el-option v-for="item in cityList" :label="item.name" :value="item.regionCode" :key="item.regionCode"></el-option>
                    </el-select>
                </el-col>
                <el-col :span="8">
                    <el-select v-model="query.districtCode" placeholder="区">
                        <el-option v-for="item in districtList" :label="item.name" :value="item.regionCode" :key="item.regionCode"></el-option>
                    </el-select>
                </el-col>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="queryList(1)">查询</el-button>
                <el-button type="primary" @click="regionTree()">区域</el-button>
            </el-form-item>
        </el-form>
        <el-table align="center" size="small"
                :data="tableData" border
                style="width: 90%;margin-left: 50px;">
            <el-table-column align="center"label="状态">
                <template slot-scope="scope">
                    <img width="230px" @click="bigPhoto(scope.row.photoUrl)" height="160px" :src="scope.row.photoUrl"/>
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

        <el-dialog :visible.sync="bigImageDialog" :before-close="bigImageClose">
            <div style="padding-top: 5px;margin-bottom: 5px">
                <i class="el-icon-zoom-in" @click="imgW=imgW+200"></i>
                <i class="el-icon-zoom-out" @click="imgW=imgW-100"></i>
            </div>
            <div>
                <img :src="bigImageUrl" :style="{height:imgW+'px'}" style="text-align: center;">
            </div>
        </el-dialog>

        <el-dialog
                title="区域分析"
                :visible.sync="visible.treeVisible"
                width="85%"
                :before-close="treeClose">
            <div id="regionalAnalyze" style="text-align: center">

            </div>
        </el-dialog>

    </template>

</div>

<jsp:include page="commonJs.jsp"/>

<script type="text/javascript" src="${webPath}/js/region.js"></script>

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
                    provinceCode: null,
                    cityCode: null,
                    districtCode: null,
                },
                provinceList: [],
                cityList: [],
                districtList: [],
                time : null,
                bigImageDialog: false,
                imgW: 500,
                bigImageUrl: null,
                visible:{
                    treeVisible: false,
                }
            };
        },
        created: function () {
            $.ajax({
                type: 'GET',
                url: _path + '/region/provinceList',
                success: function (result) {
                    if (result.code === '0') {
                        vm.provinceList = result.data;
                    }
                }
            });
        },
        mounted:function() {
            this.queryList(1);
        },
        methods: {
            regionTree() {
                initTree();
            },
            treeClose() {
                this.visible.treeVisible = false;
            },
            bigImageClose() {
                this.imgW =  500;
                this.bigImageDialog = false;
            },
            bigPhoto(url) {
                this.bigImageDialog = true;
                this.bigImageUrl = url;
            },
            selProvince(regionCode) {
                this.query.provinceCode = regionCode;
                let code = this.query.provinceCode;
                $.ajax({
                    type: 'POST',
                    url: _path + '/region/getChildren',
                    data: {
                        code : code
                    },
                    success: function (result) {
                        if (result.code === '0') {
                            vm.cityList = result.data;
                        }
                    }
                });
            },
            selCity(regionCode) {
                this.query.cityCode = regionCode;
                let code = this.query.cityCode;
                $.ajax({
                    type: 'POST',
                    url: _path + '/region/getChildren',
                    data: {
                        code : code
                    },
                    success: function (result) {
                        if (result.code === '0') {
                            vm.districtList = result.data;
                        }
                    }
                });
            },
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
