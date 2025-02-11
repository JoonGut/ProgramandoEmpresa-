# -*- coding: utf-8 -*-

from odoo import models, fields, api

class Proveedor(models.Model):
    _name = 'bar.proveedor'
    _description = 'Proveedor'

    name = fields.Char(string="Nombre", required=True)
    telefono = fields.Char(string="Teléfono", required=True)
    direccion = fields.Char(string="Dirección", required=True)
    email = fields.Char(string='Email')
    tipo_producto = fields.Selection([
        ('bebidas', 'Bebidas'),
        ('comida', 'Comida'),
        ('utensilios', 'Utensilios')
    ], string="Tipo de Producto")
    logo = fields.Image(string="Logo")
    fecha_registro = fields.Date(string="Fecha de Registro", default=fields.Date.today())
    historial_pedidos = fields.Text(string="Historial de Pedidos")
    pedidos = fields.One2many("bar.pedido", "proveedor_id", string="Pedidos")

class Pedido(models.Model):
    _name = 'bar.pedido'
    _description = 'Pedido de Proveedor'

    name = fields.Char(string="Número de Pedido", required=True)
    fecha_pedido = fields.Datetime(string="Fecha y Hora", required=True)
    productos = fields.Text(string="Productos Suministrados", required=True)
    cantidad = fields.Integer(string="Cantidad Total", required=True)
    precio_total = fields.Float(string="Precio Total", required=True)
    estado = fields.Selection([
        ('pendiente', 'Pendiente'),
        ('enviado', 'Enviado'),
        ('entregado', 'Entregado')
    ], string="Estado del Pedido")
    proveedor_id = fields.Many2one("bar.proveedor", string="Proveedor")

