db = db.getSiblingDB("statistics_db");
db.createUser({
  user: "root",
  pwd: "root",
  roles: [{ role: "readWrite", db: "statistics_db" }]
});
