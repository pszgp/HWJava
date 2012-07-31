
  <link rel='stylesheet' href='js/michael-dance-a9121c3/examples/barchart/barchart.css'>
  <script src='http://documentcloud.github.com/underscore/underscore.js'></script>
  <script src='https://raw.github.com/michael/data/a38f5fd92a5490dc5ab6a2e95e88ecba2e644c71/data.js'></script>
  <script src='http://code.jquery.com/jquery-1.7.2.min.js'></script>
  <script src='https://raw.github.com/michael/dance/96cb9a6384acce19202275c6dce9b7fbdac87763/dance.js'></script>

    <!-- Devices data -->
  <!--inspered by: script src='js/michael-dance-a9121c3/barchart/countries.js'></script-->
  <!-- Dance Performers -->
  <script src="js/michael-dance-a9121c3/examples/barchart/barchart.js"></script>

  <script>    
    $(function() {
      var devices_data = {
        "type": {
            "_id": "/type/device",
            "name": "Devices",
            "properties": {
            "name": {"name": "Device Name", "type": "string" },
            "uris": {"name": "Visited websites", "type": "string" },
            "nfreq": { "name": "Number of times", "type": "number" },
            "nuris": { "name": "Number of visited websites", "type": "number" }
            },
            "indexes": {
            "by_name": ["name"]
            }
        },
        "objects": [
            {
            "_id": "10.2.0.1",
            "name": "Device_1",
            "urls": {"10.2.0.2:8080":4497, "orac":6,"www.cs.nott.ac.uk":6},
            "nbytes": 4509,
            "nuris": 3
            },
        ]
        };  
      var devices = new Data.Collection(devices_data);
      window.barchart = new Barchart({});

      barchart.update(devices, "nfreq");

      // Update
      function update() {
        var uri = $('#uri').val();
        alert(uri);
        var query = {};
        if (uri) query["uris"] = [ uri ];
        var items = devices.find(query);
        alert(items);
        barchart.update(items, $('#property').val());
      }

      $('#property').change(update);
      $('#uri').change(update);
    });
  </script>

<div id='container'>
    <select id="property">
      <option value="nbytes">Data usage (Mb, per device)</option>
      <option value="nuris">Number of visited websites (per device)</option>
    </select>
    <select id="uri">
      <option value="">All</option>
      <option value="uri_1">10.2.0.2:8080</option>
      <option value="uri_2">www.cs.nott.ac.uk</option>
      <option value="uri_3">orac</option>
    </select>
    <div id='canvas'></div>
  </div>
  
  