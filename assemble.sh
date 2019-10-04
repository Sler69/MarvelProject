export MARVEL_API_KEY=<api-key>
export MARVEL_PRI_KEY=<private-key>
mongo --eval "db.getSiblingDB('marvel').createCollection('characters')"
