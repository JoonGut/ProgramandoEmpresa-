# -*- coding: utf-8 -*-
# from odoo import http


# class Bar(http.Controller):
#     @http.route('/bar/bar', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/bar/bar/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('bar.listing', {
#             'root': '/bar/bar',
#             'objects': http.request.env['bar.bar'].search([]),
#         })

#     @http.route('/bar/bar/objects/<model("bar.bar"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('bar.object', {
#             'object': obj
#         })

