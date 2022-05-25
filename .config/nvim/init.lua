require "plugin"
require "color"
require "mapping"
require "completion"

local options = {
  foldmethod = "expr",
  foldexpr = "nvim_treesitter#foldexpr()",
  foldenable = true,
  foldlevel = 99,

  mouse = 'a',
  number = true,

  expandtab = true,
  smarttab = true,
  shiftwidth = 2,
  tabstop = 2,
  textwidth = 0,
  softtabstop = 2,

  cursorline = true,
  cursorcolumn = false,
  autoindent = true,

  spell = true,
  spelllang = { 'en_us' },
  ignorecase = true,
}

require "treesitter"

vim.opt.shortmess:append "c"

for k, v in pairs(options) do
  vim.opt[k] = v
end
