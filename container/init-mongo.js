use admin;

db.createUser(
    {
        user: "crawler",
        pwd: "crawler123",
        roles: [
            {
                role: "readWrite",
                db: "crawler"
            }
        ]
    }
);

db.createRole(
    {
        role: "flinkrole",
        privileges: [{
            // Grant privileges on all non-system collections in all databases
            resource: { db: "", collection: "" },
            actions: [
                "splitVector",
                "listDatabases",
                "listCollections",
                "collStats",
                "find",
                "changeStream" ]
        }],
        roles: [
            // Read config.collections and config.chunks
            // for sharded cluster snapshot splitting.
            { role: 'read', db: 'config' }
        ]
    }
);

db.grantRolesToUser(
    "crawler",
    [
        { role: "flinkrole", db: "admin" }
    ]
)

