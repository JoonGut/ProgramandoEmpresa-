# -*- coding: utf-8 -*-
# from odoo import http


# class Birthday(http.Controller):
#     @http.route('/birthday/birthday', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/birthday/birthday/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('birthday.listing', {
#             'root': '/birthday/birthday',
#             'objects': http.request.env['birthday.birthday'].search([]),
#         })

#     @http.route('/birthday/birthday/objects/<model("birthday.birthday"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('birthday.object', {
#             'object': obj
#         })

