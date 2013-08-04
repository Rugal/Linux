
const Gio = imports.gi.Gio;
const Lang = imports.lang;
const St = imports.gi.St;
const GLib = imports.gi.GLib;
const Shell = imports.gi.Shell;

const Gettext = imports.gettext.domain('gnome-shell-extensions');
const _ = Gettext.gettext;
const ExtensionsPath = GLib.build_filenamev([global.userdatadir, 'extensions/']);

const Main = imports.ui.main;
const Panel = imports.ui.panel;
const PanelMenu = imports.ui.panelMenu;
const PopupMenu = imports.ui.popupMenu;

const ExtensionUtils = imports.misc.extensionUtils;
const Me = ExtensionUtils.getCurrentExtension();
const Convenience = Me.imports.convenience;

const ESMenuItem = new Lang.Class({
    Name: 'ESMenu.ESMenuItem',
    Extends: PopupMenu.PopupBaseMenuItem,

    _init: function(esmenu) {
	this.parent();

	this.label = new St.Label({ text: esmenu.get_name() });
	this.addActor(this.label);
        this.actor.label_actor = this.label;

	this.esmenu = esmenu;

	let ESIcon = new St.Icon({ icon_name: 'preferences-other-symbolic',
				      style_class: 'popup-menu-icon ' });
	let ESButton = new St.Button({ child: ESIcon });
	this.addActor(ESButton);
    }

});

const ESMenu = new Lang.Class({
    Name: 'ESMenu.ESMenu',
    Extends: PanelMenu.SystemStatusButton,

    _init: function() {
	this.parent('preferences-other-symbolic', _("ESMenuItem"));

	this._buildMenu();

    },

    _buildMenu: function()
	{    			
		this.ie = new PopupMenu.PopupMenuItem("Installed Extensions");
		this.menu.addMenuItem(this.ie);
		this.ie.connect('activate', Lang.bind(this, this._doLocalExtensions));

		this.ae = new PopupMenu.PopupMenuItem("Browse Extensions");
		this.menu.addMenuItem(this.ae);
		this.ae.connect('activate', Lang.bind(this, this._doAllExtensions));

		this.pf = new PopupMenu.PopupMenuItem("Extension Preferences");
		this.menu.addMenuItem(this.pf);
		this.pf.connect('activate', Lang.bind(this, this._doExtensionPreferences));

		this.gt = new PopupMenu.PopupMenuItem("Advanced Settings");
		this.menu.addMenuItem(this.gt);
		this.gt.connect('activate', Lang.bind(this, this._doTweakTool));


		this.menu.addMenuItem(new PopupMenu.PopupSeparatorMenuItem());

		this.ef = new PopupMenu.PopupMenuItem("Open Extensions Folder");
		this.menu.addMenuItem(this.ef);
		this.ef.connect('activate', Lang.bind(this, this._doLocalFolder));

		this.rs = new PopupMenu.PopupMenuItem("Restart Shell");
		this.menu.addMenuItem(this.rs);
		this.rs.connect('activate', Lang.bind(this, this._restartShell));

		this.lg = new PopupMenu.PopupMenuItem("Looking Glass");
		this.menu.addMenuItem(this.lg);
		this.lg.connect('activate', Lang.bind(this, this._lookingGlass));
	},

	_lookingGlass: function() {
		Main.createLookingGlass().toggle();
	},

	_doLocalExtensions: function() {
		Main.Util.trySpawnCommandLine('xdg-open https://extensions.gnome.org/local/');
	},

	_doAllExtensions: function() {
		Main.Util.trySpawnCommandLine('xdg-open https://extensions.gnome.org/');
	},

	_doLocalFolder: function() {
		Main.Util.trySpawnCommandLine('xdg-open ' + ExtensionsPath);
	},

	_doExtensionPreferences: function() {
		Main.Util.trySpawnCommandLine('gnome-shell-extension-prefs');
	},

	_doTweakTool: function() {
		try {
			Main.Util.trySpawnCommandLine('gnome-tweak-tool');
		} catch(e) {
			Main.notify('gnome-tweak-tool is required. Install it to access advanced settings.');
		}
	},

	_restartShell: function() {
		global.reexec_self();
	},

    destroy: function() {
		this.parent();
    },
});

function init() {
    Convenience.initTranslations();
}

let _indicator;

function enable() {
    _indicator = new ESMenu;
    Main.panel.addToStatusArea('ES-menu', _indicator);
}

function disable() {
    _indicator.destroy();
}
