/* D3 constants */
let svg;
let path;

function render(p) {
    if (d3.select('.graph')) d3.select('.graph').remove();

    d3.select('g')
        .append("path")
        .datum(p)
        .attr("class", "graph")
        .attr("fill", "none")
        .attr("stroke", "steelblue")
        .attr("stroke-width", 1.5)
        .attr("d", d3.line()
            .x(function (d) { return x(d.mes) })
            .y(function (d) { return y(d.intereses) })
        )
}

/* D3 */
// set the dimensions and margins of the graph
var margin = { top: 10, right: 30, bottom: 30, left: 60 },
    width = 900 - margin.left - margin.right,
    height = 400 - margin.top - margin.bottom;

// append the svg object to the body of the page
svg = d3.select("#interes_graph")
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform",
        "translate(" + margin.left + "," + margin.top + ")");

// Add X axis --> it is a date format
var x = d3.scaleLinear()
    .domain([0, meses])
    .range([0, width]);
svg.append("g")
    .attr("transform", "translate(0," + height + ")")
    .call(d3.axisBottom(x));

// Add Y axis
var y = d3.scaleLinear()
    .domain([0, capital * interes / 12 * 1.1])
    .range([height, 0]);
svg.append("g")
    .call(d3.axisLeft(y));