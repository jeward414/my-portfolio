window.onload = function() {
    drawChart();
}

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

/** Fetches color data and uses it to create a chart. */
function drawChart() {
  fetch('/chart-data').then(response => response.json())
  .then((gameVotes) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Game');
    data.addColumn('number', 'Votes');
    Object.keys(gameVotes).forEach((game) => {
      data.addRow([game, gameVotes[game]]);
    });

    const options = {
      'width':500,
      'height':500,
      'backgroundColor': '#caf0f8' 
    };

    const chart = new google.visualization.PieChart(
        document.getElementById('videogame-container'));
    chart.draw(data, options);
  });
}