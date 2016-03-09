'use strict';

describe('PromptService', function () {

    beforeEach(module('SgmapRetraiteCommons'));

    var promptOptionsCalled;
    var prompt = function(options) {
        promptOptionsCalled = options;
        return 'return promise on prompt';
    };
    
    beforeEach(module(function($provide) {
        $provide.factory('prompt', function() {
            return prompt;
        });
    }));

    var PromptService;

    beforeEach(inject(function (_PromptService_) {
        PromptService = _PromptService_;

        promptOptionsCalled = undefined;
    }));

    it('should prompt simple information', function () {

        var result = PromptService.promptInformation("mon titre", "mon message");

        expect(result).toEqual('return promise on prompt');
        expect(promptOptionsCalled).toEqual({
            title: "mon titre",
            message: "mon message",
            buttons: [{ label:'OK', primary: true }]
        });
    });

    it('should prompt simple questions', function () {

        var result = PromptService.promptQuestion("mon titre", "mon message");

        expect(result).toEqual('return promise on prompt');
        expect(promptOptionsCalled).toEqual({
            title: "mon titre",
            message: "mon message"});
    });

});
