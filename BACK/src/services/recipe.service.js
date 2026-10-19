const mongoose = require("mongoose");

const Recipe = require("../model/recipe.model");
const Ingredient = require("../model/ingredient.model");

function validateId(id) {
    if (!mongoose.Types.ObjectId.isValid(id)) {
        const error = new Error("Identificador no válido");
        error.status = 400;
        throw error;
    }
}

async function validateIngredients(ingredients) {
    for (const item of ingredients || []) {
        const ingredientId = typeof item.ingredient === 'object' && item.ingredient !== null ? item.ingredient._id : item.ingredient;

        if (!mongoose.Types.ObjectId.isValid(ingredientId)) {
            const error = new Error(`ID de ingrediente no válido: ${ingredientId}`);

            error.status = 400;
            throw error;
        }

        const exists = await Ingredient.exists({_id: ingredientId});

        if (!exists) {
            const error = new Error(`No existe el ingrediente ${ingredientId}`);

            error.status = 400;
            throw error;
        }
    }
}

async function getAll() {
    return Recipe.find()
        .populate("ingredients.ingredient")
        .sort({ name: 1 });
}

async function getById(id) {
    validateId(id);

    const recipe = await Recipe.findById(id)
        .populate("ingredients.ingredient");

    if (!recipe) {
        const error = new Error("Receta no encontrada");
        error.status = 404;
        throw error;
    }

    return recipe;
}

async function create(data) {
    const ingredients = (data.ingredients || []).map(item => ({
        ...item,
        ingredient:
            typeof item.ingredient === 'object' && item.ingredient !== null ? item.ingredient._id : item.ingredient
    }));

    await validateIngredients(ingredients);

    const recipe = new Recipe({...data, ingredients});

    await recipe.validate();
    await recipe.save();

    return recipe.populate('ingredients.ingredient');
}

async function update(id, data) {
    validateId(id);

    const recipe = await Recipe.findById(id);

    if (!recipe) {
        const error = new Error("Receta no encontrada");
        error.status = 404;
        throw error;
    }

    if (data.name !== undefined) {
        recipe.name = data.name;
    }

    if (data.description !== undefined) {
        recipe.description = data.description;
    }

    if (data.ingredients !== undefined) {
        await validateIngredients(data.ingredients);
        recipe.ingredients = data.ingredients;
    }

    if (data.steps !== undefined) {
        recipe.steps = data.steps;
    }

    await recipe.validate();
    await recipe.save();

    return recipe.populate("ingredients.ingredient");
}

async function remove(id) {
    validateId(id);

    const recipe = await Recipe.findByIdAndDelete(id);

    if (!recipe) {
        const error = new Error("Receta no encontrada");
        error.status = 404;
        throw error;
    }

    return recipe;
}

module.exports = {
    getAll,
    getById,
    create,
    update,
    remove
};