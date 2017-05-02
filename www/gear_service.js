/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var gear_service =
        {
            handle: function (GearServiceSuccessHandler, GearServiceErrorHandler, action, routeData)
            {
                cordova.exec(
                        GearServiceSuccessHandler,
                        GearServiceErrorHandler,
                        'OnBootManagerPlugin',
                        action,
                        []
                            );
            }
        };
        
        module.exports = gear_service;