const mongoose = require("mongoose");

const recipeIngredientSchema = new mongoose.Schema(
    {
        ingredient: {
            type: mongoose.Schema.Types.ObjectId,
            ref: "Ingredient",
            required: true
        },
        quantity: {
            type: Number,
            required: true,
            min: 0.01
        }
    },
    {
        _id: false
    }
);

const stepSchema = new mongoose.Schema(
    {
        text: {
            type: String,
            required: true,
            trim: true
        },
        time: {
            type: Number,
            required: true,
            min: 0
        }
    }
);

const recipeSchema = new mongoose.Schema(
    {
        name: {
            type: String,
            required: true,
            trim: true
        },
        description: {
            type: String,
            default: "",
            trim: true
        },
        ingredients: {
            type: [recipeIngredientSchema],
            default: []
        },
        steps: {
            type: [stepSchema],
            default: []
        },
        preparationTime: {
            type: Number,
            default: 0,
            min: 0
        }
    },
    {
        timestamps: true
    }
);

recipeSchema.pre("validate", function () {
    this.preparationTime = this.steps.reduce(
        (total, step) => total + step.time,
        0
    );
});

module.exports = mongoose.model(
    "Recipe",
    recipeSchema
);