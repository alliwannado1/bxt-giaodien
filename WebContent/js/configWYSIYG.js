CKEDITOR.editorConfig = function(config) {
	config.resize_enabled = false;
	config.toolbarGroups = [
	                        { name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
	                        { name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
	                        { name: 'links' },
	                        { name: 'insert' },
	                        { name: 'tools' },
	                        { name: 'document',    groups: [ 'mode', 'document', 'doctools' ] },
	                        { name: 'others' },
	                        '/',
	                        { name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
	                        { name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align' ] },
	                        { name: 'styles' },
	                        { name: 'colors' },
	                        { name: 'about' }
	                    ];
//	config.toolbar = 'Simple';
//	config.toolbar_Simple = [ [ 'Bold', 'Italic', '-', 'NumberedList',
//			'BulletedList', '-', 'Link', 'Unlink', '-', 'About' ] ];
//	config.toolbar_Complex = [ ['Styles', 'Format', 'Font',
//	                 			'FontSize', 'Bold', 'Italic', 'Underline', 'Strike',
//			'TextColor', 'Link', 'Unlink', 'Undo', 'Redo',
//			'PageBreak', 'JustifyLeft', 'JustifyCenter', 'JustifyRight',
//			'JustifyBlock','SpecialChar'] ];
	//config.skin = 'kama';
	//console.log("a");

};