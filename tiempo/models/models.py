# -*- coding: utf-8 -*-

from odoo import models, fields, api

class Previsiones(models.Model):
    _name = 'tiempo.previsiones'
    _description = 'Prevision Atmosferica'
    
    name = fields.Char(string="Nombre de la Previsión", required=True)
    ciudad = fields.Char(string="Ciudad", required=True)
    temp_max = fields.Float(string="Temperatura Máxima", required=True)
    temp_min = fields.Float(string="Temperatura Mínima", required=True)
    humedad = fields.Integer(string="Humedad (%)")
    precipitaciones = fields.Float(string="Precipitaciones (mm)")
    velocidad_viento = fields.Float(string="Velocidad del Viento (km/h)")
    promedio_temperaturas = fields.Float(
        string="Promedio de Temperaturas", 
        compute="_compute_promedio_temperaturas", 
        store=True
    )
    estado_tiempo = fields.Char(string="Estado del Tiempo")
    fecha_prevision = fields.Date(string="Fecha de la Previsión")
    alertas_ids = fields.One2many(
        'tiempo.alertas', 
        'prevision_id', 
        string="Alertas Meteorológicas"
    )

    @api.depends('temp_max', 'temp_min')
    def _compute_promedio_temperaturas(self):
        for record in self:
            if record.temp_max and record.temp_min:
                record.promedio_temperaturas = (record.temp_max + record.temp_min) / 2
            else:
                record.promedio_temperaturas = 0
                
                
class Alertas(models.Model):
    _name = 'tiempo.alertas'
    _description = 'Alerta Meteorologica'

    name = fields.Char(string="Alerta", required=True)
    descripcion = fields.Text(string="Descripción")
    prevision_id = fields.Many2one(
        'tiempo.previsiones', 
        string="Previsión Atmosférica",
        ondelete='cascade'
    )
