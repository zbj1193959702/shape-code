<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp" %>
<%@include file="/common/path.jsp" %>
<html>
<head>
    <title>地图</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="referrer" content="no-referrer" />
    <link rel="stylesheet" media="screen" href="../../../css/bootstrap.min.css">
    <link rel="stylesheet" href="http://outter-common.toodc.cn/static-source/theme-chalk-index_2.13.0.css"/>
</head>
<style>
    #map-3d {
        position: absolute;
        top: 0;
        bottom: 0;
        width: 90%;
    }
</style>
<body>

<div id='map-3d'></div>

<jsp:include page="commonJs.jsp"/>

<script src="https://webapi.amap.com/maps?v=1.4.15&key=0b49a5b1f351d9c9e52406e1f4afc858&plugin=Map3D&plugin=AMap.ControlBar,AMap.MapType"></script>
<script src="https://webapi.amap.com/ui/1.1/main.js"></script>

<script>
    let map;
    let floorLineC = [9 / 255, 0 / 255, 0 / 255, 0.99];
    // let fc = "rgb(6,221,255)";
    let object3Dlayer;
    $(function () {
        let thisLon = 116.39756;
        let thisLat = 39.904215;
        let hasUnAreaColor = [10 / 255, 183 / 255, 168 / 255, 0.66];
        // 创建 3D 底图
        map = new AMap.Map('map-3d', {
            viewMode: '3D', // 开启 3D 模式
            pitch: 55,
            rotation: 35,
            center: [thisLon, thisLat],
            features: ['bg', 'road'],
            zoom: 17,
            mapStyle: "amap://styles/fresh"
        });

        map.addControl(new AMap.ControlBar());
        map.AmbientLight = new AMap.Lights.AmbientLight([1, 1, 1], 0.5);
        map.DirectionLight = new AMap.Lights.DirectionLight([1, -1, 2], [1, 1, 1], 0.8);

        let paths = [
            [116.395951,39.907129],
            [116.399127,39.907178],
            [116.399534,39.900413],
            [116.396316,39.900331]
        ];
        object3Dlayer = new AMap.Object3DLayer();
        map.add(object3Dlayer);

        let bounds = paths.map(function (p) {
            return new AMap.LngLat(p[0], p[1]);
        });
        let baseLine = drawLine(bounds, 400, floorLineC, 1);
        object3Dlayer.add(baseLine);

        let baseMesh = drawMiddleLayer(bounds, hasUnAreaColor, 400, 405, false);
        object3Dlayer.add(baseMesh);

        let mesh = drawMiddleLayer(bounds, hasUnAreaColor, 400, 800, true);
        object3Dlayer.add(mesh);

        let lineHigh = drawLine(bounds, 1000, floorLineC, 2);
        object3Dlayer.add(lineHigh);
    });

    function drawMiddleLayer(bounds, color, startH, endH, transparent) {
        let mesh = new AMap.Object3D.Mesh();
        let geometry = mesh.geometry;
        let vertices = geometry.vertices;
        let vertexColors = geometry.vertexColors;
        let faces = geometry.faces;
        let vertexLength = bounds.length * 2;

        let verArr = [];
        bounds.forEach(function (lngLat, index) {
            let g20 = map.lngLatToGeodeticCoord(lngLat);
            verArr.push([g20.x, g20.y]);
            // 构建顶点-底面顶点
            vertices.push(g20.x, g20.y, -startH);
            // 构建顶点-顶面顶点
            vertices.push(g20.x, g20.y, -endH);
            vertexColors.push.apply(vertexColors, color);
            vertexColors.push.apply(vertexColors, color);
            let bottomIndex = index * 2;
            let topIndex = bottomIndex + 1;
            let nextBottomIndex = (bottomIndex + 2) % vertexLength;
            let nextTopIndex = (bottomIndex + 3) % vertexLength;
            //侧面三角形1
            faces.push(bottomIndex, topIndex, nextTopIndex);
            //侧面三角形2
            faces.push(bottomIndex, nextTopIndex, nextBottomIndex);

            drawVerticalBar(lngLat, endH);
        });
        // 物业描边 不使用黑色
       let line = drawLine(bounds, endH, floorLineC, 1);
       object3Dlayer.add(line);

        // 设置顶面，根据顶点拆分三角形
        let triangles = AMap.GeometryUtil.triangulateShape(verArr);
        for (let v = 0; v < triangles.length; v += 3) {
            let a = triangles[v];
            let b = triangles[v + 2];
            let c = triangles[v + 1];
            faces.push(a * 2 + 1, b * 2 + 1, c * 2 + 1);
        }
        mesh.backOrFront = 'both';
        mesh.transparent = transparent;
        return mesh;
    }

    function drawVerticalBar(lngLat, endH) {
        let Line3D = new AMap.Object3D.Line();
        let Lgeometry = Line3D.geometry;
        let origin = map.lngLatToGeodeticCoord(lngLat);
        Lgeometry.vertices.push(origin.x, origin.y, -endH);
        Lgeometry.vertexColors.push(9 / 255, 0 / 255, 0 / 255, 0.99);

        let des = map.lngLatToGeodeticCoord(lngLat);
        Lgeometry.vertices.push(des.x, des.y, 0);
        Lgeometry.vertexColors.push(9 / 255, 0 / 255, 0 / 255, 0.99);
        object3Dlayer.add(Line3D);
    }

    function drawLine(bounds, height, color, lineW) {
        if (bounds == null || bounds.length === 0) {
            return;
        }
        let lineBounds = [];
        let lineHeight = [];
        for (let i = 0; i < bounds.length; i++) {
            lineHeight.push(height);
            lineBounds.push(bounds[i]);
        }
        lineHeight.push(height);
        lineBounds.push(bounds[0]);
        return new AMap.Object3D.MeshLine({
            path: lineBounds,
            height: lineHeight,
            color: color,
            width: lineW
        });
    }

</script>
</body>
</html>
