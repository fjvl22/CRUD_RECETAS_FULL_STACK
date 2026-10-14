const service = require("../services/recipe.service");

async function getAll(req, res) {
    res.json(await service.getAll());
}

async function getById(req, res) {
    res.json(await service.getById(req.params.id));
}

async function create(req, res) {
    res.status(201).json(
        await service.create(req.body)
    );
}

async function update(req, res) {
    res.json(
        await service.update(req.params.id, req.body)
    );
}

async function remove(req, res) {
    await service.remove(req.params.id);
    res.status(204).send();
}

module.exports = {
    getAll,
    getById,
    create,
    update,
    remove
};