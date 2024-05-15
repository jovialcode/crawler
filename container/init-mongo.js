

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