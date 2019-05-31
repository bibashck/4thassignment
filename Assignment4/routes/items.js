var express = require('express');
var items = require('../models/items');
var verify = require('../verify');
// var fs = require('fs');
var router = express.Router();

router.route('/')
    .get((req, res, next) => {
        items.find({})
            .then((items) => {
                res.statusCode = 200;
                res.setHeader('Content-Type', 'application/json');
                res.json(items);
            }, (err) => next(err))
            .catch((err) => next(err));
    })
    .post((req, res, next) => {
        items.create(req.body)
            .then((item) => {
                res.statusCode = 200;
                res.setHeader('Content-Type', 'application/json');
                res.json(item);
            }, (err) => next(err))
            .catch((err) => next(err));
    })
    .put((req, res, next) => {
        res.statusCode = 403;
        res.end('PUT operation not supported!');
    })
    .delete((req, res, next) => {
        items.deleteMany({})
            .then((reply) => {
                res.statusCode = 200;
                res.setHeader('Content-Type', 'application/json');
                res.json(reply);
            }, (err) => next(err))
            .catch((err) => next(err));
    });

router.route('/:id')
    .get((req, res, next) => {
        items.findById(req.params.id)
            .then((item) => {
                res.statusCode = 200;
                res.setHeader('Content-Type', 'application/json');
                res.json(item);
            }, (err) => next(err))
            .catch((err) => next(err));
    })
    .post((req, res, next) => {
        res.statusCode = 403;
        res.end("POST operation not supported!");
    })
    // .put((req, res, next) => {
    //     items.findByIdAndUpdate(req.params.id, { $set: req.body }, { new: true, useFindAndModify: false })
    //         .then((item) => {
    //             res.statusCode = 200;
    //             res.setHeader('Content-Type', 'application/json');
    //             res.json(item);
    //         }, (err) => next(err))
    //         .catch((err) => next(err));
    // })
    // .delete((req, res, next) => {
    //     items.findByIdAndDelete(req.params.id)
    //         .then((reply) => {
    //             res.statusCode = 200;
    //             res.setHeader('Content-Type', 'application/json');
    //             res.json(reply);
    //         }, (err) => next(err))
    //         .catch((err) => next(err));
    // });

module.exports = router;