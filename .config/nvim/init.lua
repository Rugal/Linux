require "plugin"
require "color"
require "mapping"
require "completion"
require "autocommand"

local options = {
  foldmethod = "expr",
  foldexpr = "nvim_treesitter#foldexpr()",
  foldenable = true,
  foldlevel = 99,

  ff="unix",

  mouse = 'a',
  number = true,
  relativenumber = true,

  expandtab = true,
  smarttab = true,
  shiftwidth = 2,
  tabstop = 2,
  textwidth = 0,
  softtabstop = 2,

  list = true,
  listchars = "eol:$,tab:>-,trail:~,extends:>,precedes:<",

  cursorline = true,
  cursorcolumn = false,
  autoindent = true,

  spell = true,
  spelllang = { 'en_us' },
  ignorecase = true,
}

vim.opt.shortmess:append "c"

for k, v in pairs(options) do
  vim.opt[k] = v
end
