# -*- coding: utf-8 -*-
# from odoo import http


# class Tiempo(http.Controller):
#     @http.route('/tiempo/tiempo', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/tiempo/tiempo/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('tiempo.listing', {
#             'root': '/tiempo/tiempo',
#             'objects': http.request.env['tiempo.tiempo'].search([]),
#         })

#     @http.route('/tiempo/tiempo/objects/<model("tiempo.tiempo"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('tiempo.object', {
#             'object': obj
#         })

