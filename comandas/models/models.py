# -*- coding: utf-8 -*-

from odoo import models, fields, api


class Comanda(models.Model):
    _name = 'comandas.comanda'
    _description = 'Comandas'
    _order = 'fecha_hora desc'
    
    nombre = fields.Char(string='Número de Comanda', required=True, copy=False, default='Nuevo')
    mesa_id = fields.Many2one('comandas.mesa', string='Mesa', required=True)
    cantidad_articulos = fields.Float(string='Cantidad de Artículos', required=True)
    camarero_id = fields.Many2one('comandas.camarero', string='Camarero', required=True)
    total = fields.Float(string='Total', compute='_compute_total', store=True)
    fecha_hora = fields.Datetime(string='Fecha y Hora', default=lambda self: fields.Datetime.now())

    @api.depends('cantidad_articulos')
    def _compute_total(self):
        for record in self:
            record.total = record.cantidad_articulos * 2 if record.cantidad_articulos else 0.0


class Mesa(models.Model):
    _name = 'comandas.mesa'
    _description = 'Mesas'
    
    id_mesa = fields.Char(string='ID de Mesa', required=True, copy=False, default='Nuevo')
    nombre = fields.Char(string='Número de Mesa', required=True)
    estado = fields.Selection([
        ('libre', 'Libre'),
        ('ocupada', 'Ocupada'),
    ], string='Estado', default='libre')


class Camarero(models.Model):
    _name = 'comandas.camarero'
    _description = 'Camareros'
    
    id_camarero = fields.Char(string='ID de Camarero', required=True, copy=False, default='Nuevo')
    nombre = fields.Char(string='Nombre del Camarero', required=True)
    telefono = fields.Char(string='Número de Teléfono')

