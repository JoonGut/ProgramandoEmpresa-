import logging
from datetime import datetime
from odoo import models, fields, api

class EmpleadoBirthday(models.Model):
    _name = 'birthday.empleado_birthday'  # Tu modelo propio
    _description = 'Mensaje de Cumpleaños'

    empleado_id = fields.Many2one('hr.employee', string="Empleado", required=True)
    envio_mensage_birthday = fields.Boolean(
        string='Envío mensaje cumpleaños',
        help="Indica si ya se ha enviado el mensaje de cumpleaños."
    )

    @api.model
    def comparacion_birthdays(self):
        """ Busca empleados con cumpleaños hoy y les envía un email. """
        # Obtener día y mes actual
        dia_actual = datetime.today().date().strftime('%m-%d')  # Día y mes en formato MM-DD

        empleados = self.env['hr.employee'].search([
            ('birthday', '!=', False),
        ])

        for empleado in empleados:
            # Comparar solo el día y el mes
            if empleado.birthday.strftime('%m-%d') == dia_actual:
                # Verificar si ya se ha enviado el mensaje
                existe_registro = self.search([
                    ('empleado_id', '=', empleado.id),
                    ('envio_mensage_birthday', '=', True)
                ])
                if not existe_registro:
                    self.envio_birthday_email(empleado)
                    # Solo crear un registro si realmente es necesario
                    self.create({
                        'empleado_id': empleado.id,
                        'envio_mensage_birthday': True
                    })

    def envio_birthday_email(self, empleado):
        """ Envía un email de cumpleaños al empleado. """
        template = self.env.ref('birthday.bdayemail')
        if template:
            email_from = empleado.company_id.email 
            email_to = empleado.work_email 

            try:
                template.send_mail(empleado.id, force_send=True, email_values={
                    'email_from': email_from,
                    'email_to': email_to,
                    'object': empleado.name,
                    'author_id': self.env.user.partner_id.id,
                })

