function init() {
    registerStatsConsumer();
    registerGraph("graph1", "lifetime", PERCENTILES);
    initGraph();
};

function registerStatsConsumer() {
    var eventBus = new EventBus(getUrl() + '/eventbus');
    eventBus.onopen = function() {
        console.log('Connected')
        eventBus.registerHandler("graph1", function(dummy, message) {
            console.log('got it! :) ' + message.body);
            refreshData(JSON.parse(message.body));
            updateGraph();
        });
    };
};

function getUrl() {
    return window.location.protocol + '//' + window.location.hostname + ':' + window.location.port;
}

PERCENTILES = ["10", "20", "30", "40", "50", "60", "70", "80", "90", "95", "99", "99.9", "99.99", "99.999"];
config = {}

function refreshData(stats) {
    config.data.datasets[0].data = stats.processedAveragePercentiles;
    config.data.datasets[1].data = stats.receivedAveragePercentiles;
    config.data.datasets[2].data = stats.serviceTimeAveragePercentiles;
}

function registerGraph(graphName, statsType, percentiles) {

    var request = createRequest();
    request.open("POST", getUrl() + '/graph/' + statsType);
    request.setRequestHeader("Content-Type", "application/json");
    request.send(JSON.stringify({graphName: graphName}));

}

function unregisterGraph(graphName, statsType, percentiles) {

    var request = createRequest();
    request.open("DELETE", getUrl() + '/graph/' + statsType);
    request.setRequestHeader("Content-Type", "application/json");
    request.send(JSON.stringify({graphName: graphName}));

}

function resetStats(graphName) {

    var request = createRequest();
    request.open("PATCH", getUrl() + '/graph/lifetime');
    request.setRequestHeader("Content-Type", "application/json");
    request.send(JSON.stringify({}));

}

function createRequest() {
    var xmlhttp = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4) {
            if (xmlhttp.status != 200) {
                console.log('Sorry, something went wrong.');
            }
        }
    };
    return xmlhttp;
}

function initGraph() {
    config = {
        type: 'line',
        data: {
            labels: PERCENTILES,
            datasets: [{
                label: "Response time",
                backgroundColor: window.chartColors.red,
                borderColor: window.chartColors.red,
                data: new Array(PERCENTILES.length).fill(0),
                fill: false,
            },
            {
                label: "Receive time",
                backgroundColor: window.chartColors.blue,
                borderColor: window.chartColors.blue,
                data: new Array(PERCENTILES.length).fill(0),
                fill: false,
            },
            {
                label: "Service time",
                backgroundColor: window.chartColors.yellow,
                borderColor: window.chartColors.yellow,
                data: new Array(PERCENTILES.length).fill(0),
                fill: false,
            }]
        },
        options: {
            responsive: true,
            title:{
                display:true,
                text:'Percentile graph'
            },
            tooltips: {
                mode: 'index',
                intersect: false,
                enabled: false
            },
            hover: {
                mode: 'nearest',
                intersect: false,
                animationDuration: 200
            },
            scales: {
                xAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'percentile'
                    }
                }],
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'millis'
                    },
                    pointLabels: {

                    },
                    ticks: {
                        min: 0,
                        suggestedMax: 100
                    }
                }]
            }
        }
    };
    var ctx = document.getElementById("canvas").getContext("2d");
    window.graph = new Chart(ctx, config);
};

function updateGraph() {
    window.graph.update();
};

window.chartColors = {
    red: 'rgb(255, 99, 132)',
    orange: 'rgb(255, 159, 64)',
    yellow: 'rgb(255, 205, 86)',
    green: 'rgb(75, 192, 192)',
    blue: 'rgb(54, 162, 235)',
    purple: 'rgb(153, 102, 255)',
    grey: 'rgb(201, 203, 207)'
};

function adjustScale() {
    config.options.scales.yAxes[0].ticks.suggestedMax = config.data.datasets[0].data[config.data.datasets[0].data.length - 1] * 1.2;
}

function setScale(event, params) {
    var newScale = document.getElementById('scale').value;
    if (newScale > 0) {
        config.options.scales.yAxes[0].ticks.suggestedMax = newScale;
    }
}

function createSnapshot() {
    var link = document.createElement('a');
    link.href = window.graph.toBase64Image();
    link.download = "percentile_graph.png";

    var evt = document.createEvent('MouseEvents');
    evt.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);

    link.dispatchEvent(evt);

}