<%@ page %><%
    String webPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    String path = request.getContextPath();
    webPath += path;
    request.setAttribute("webPath", webPath);
%>
<script type="text/javascript">
    const _path="<%=webPath%>";
</script>
