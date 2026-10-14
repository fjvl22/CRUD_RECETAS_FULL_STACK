require("dotenv").config();

const express = require("express");
const mongoose = require("mongoose");

const recipeRoutes = require("./src/routes/recipe.routes");
const ingredientRoutes = require("./src/routes/ingredient.routes");

const app = express();

app.use(express.json());

app.use("/api/recipes", recipeRoutes);
app.use("/api/ingredients", ingredientRoutes);

app.use((error, req, res, next) => {
    console.error(error);

    if (
        error.name === "ValidationError" ||
        error.name === "CastError"
    ) {
        return res.status(400).json({
            error: error.message
        });
    }

    if (error.code === 11000) {
        return res.status(409).json({
            error: "Ya existe un ingrediente con ese nombre"
        });
    }

    return res.status(error.status || 500).json({
        error: error.message || "Error interno del servidor"
    });
});

const port = process.env.PORT || 3000;

mongoose.connect(process.env.MONGO_URI)
    .then(() => {
        console.log("Conectado a MongoDB");

        app.listen(port, () => {
            console.log(
                `Servidor escuchando en http://localhost:${port}`
            );
        });
    })
    .catch((error) => {
        console.error(
            "No se pudo conectar a MongoDB:",
            error.message
        );

        process.exit(1);
    });