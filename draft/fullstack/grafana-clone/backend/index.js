const Influx = require("influx");
const { Sequelize, Model, DataTypes } = require("sequelize");
const express = require("express");
const cors = require("cors");
const app = express();
const port = 3050;

const influx = new Influx.InfluxDB({
  host: "localhost",
  database: "telegraf",
});

const sequelize = new Sequelize("atmos", "root", "password", {
  dialect: "mysql",
  dialectOptions: {},
});

class Dashboard extends Model {}
Dashboard.init(
  {
    name: DataTypes.STRING,
  },
  { sequelize, modelName: "dashboards" }
);

class Graph extends Model {}
Graph.init(
  {
    key: DataTypes.STRING,
    label: DataTypes.STRING,
    type: DataTypes.ENUM("line", "bubble", "map"),
    unit: DataTypes.STRING,
    width: DataTypes.INTEGER,
    height: DataTypes.INTEGER,
  },
  { sequelize, modelName: "graphs" }
);

Dashboard.hasMany(Graph);

app.use(cors());

app.get("/", (req, res) => {
  res.send("Hello World!");
});

app.get("/dashboards", async (req, res) => {
  res.send(await Dashboard.findAll());
});

app.get("/dashboards/:id", async (req, res) => {
  const { id } = req.params;

  const dashboard = await Dashboard.findOne({
    where: {
      id,
    },
  });

  if (dashboard) {
    res.send(dashboard);
  } else {
    res.status(404).send({ error: "not found" });
  }
});


app.get("/dashboards/:id/graphs", async (req, res) => {
  const { id } = req.params;

  res.send(
    await Graph.findAll({
      where: {
        dashboardId: id,
      },
    })
  );
});

app.get("/graphs/:id", async (req, res) => {
  const { id } = req.params;

  const graph = await Graph.findOne({
    where: {
      id,
    },
  });

  if (graph) {
    res.send(graph);
  } else {
    res.status(404).send({ error: "not found" });
  }
});

app.get("/query", async (req, res) => {
  const { property } = req.query

  const query = await influx.query(
    `SELECT mean("${property}") FROM "cpu" WHERE time >= now() - 1h GROUP BY time(1m) fill(none)`
  );

  res.send(query.map(({ time, mean }) => [time, mean]));
});

(async () => {
  await sequelize.sync();

  app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`);
  });
})();
