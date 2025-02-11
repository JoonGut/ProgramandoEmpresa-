# -*- coding: utf-8 -*-
# from odoo import http


# class Comandas(http.Controller):
#     @http.route('/comandas/comandas', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/comandas/comandas/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('comandas.listing', {
#             'root': '/comandas/comandas',
#             'objects': http.request.env['comandas.comandas'].search([]),
#         })

#     @http.route('/comandas/comandas/objects/<model("comandas.comandas"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('comandas.object', {
#             'object': obj
#         })

