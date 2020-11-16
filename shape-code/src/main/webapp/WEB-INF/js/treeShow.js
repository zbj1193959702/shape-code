let width = 700,
    height = 700;

let cluster = d3.layout.cluster()
    .size([width, height - 200]);

let diagonal = d3.svg.diagonal()
    .projection(function(d) { return [d.y, d.x]; });

let svg = d3.select("body").append("svg")
    .attr("width", width)
    .attr("height", height)
    .append("g")
    .attr("transform", "translate(40,0)");



d3.json("d3.json", function(error, root) {

    let nodes = cluster.nodes(root);
    let links = cluster.links(nodes);

    let link = svg.selectAll(".link")
        .data(links)
        .enter()
        .append("path")
        .attr("class", "link")
        .attr("d", diagonal);

    let node = svg.selectAll(".node")
        .data(nodes)
        .enter()
        .append("g")
        .attr("class", "node")
        .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; })

    node.append("circle")
        .attr("r", 4.5);

    node.append("text")
        .attr("dx", function(d) { return d.children ? -8 : 8; })
        .attr("dy", 3)
        .style("text-anchor", function(d) { return d.children ? "end" : "start"; })
        .text(function(d) { return d.name; });
});
