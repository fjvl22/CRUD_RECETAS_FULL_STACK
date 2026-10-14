const Ingredient = require("../model/ingredient.model");

async function getAll() {
    return Ingredient.find().sort({ name: 1 });
}

async function getById(id) {
    const ingredient = await Ingredient.findById(id);

    if (!ingredient) {
        const error = new Error("Ingrediente no encontrado");
        error.status = 404;
        throw error;
    }

    return ingredient;
}

async function create(data) {
    return Ingredient.create(data);
}

async function update(id, data) {
    const ingredient = await Ingredient.findByIdAndUpdate(
        id,
        data,
        {
            new: true,
            runValidators: true
        }
    );

    if (!ingredient) {
        const error = new Error("Ingrediente no encontrado");
        error.status = 404;
        throw error;
    }

    return ingredient;
}

async function remove(id) {
    const ingredient = await Ingredient.findByIdAndDelete(id);

    if (!ingredient) {
        const error = new Error("Ingrediente no encontrado");
        error.status = 404;
        throw error;
    }

    return ingredient;
}

module.exports = {
    getAll,
    getById,
    create,
    update,
    remove
};