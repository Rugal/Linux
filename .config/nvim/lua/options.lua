require "nvchad.options"

-- add yours here!

-- local o = vim.o
-- o.cursorlineopt ='both' -- to enable cursorline!

local options = {
  fillchars = "eob: ,fold: ,foldopen:,foldsep: ,foldclose:",

  ff="unix",

  list = true,
  listchars = "eol:↴,tab:>-,trail:~,extends:>,precedes:<",

  foldmethod = 'expr',
  foldexpr = 'nvim_treesitter#foldexpr()',
  -- relativenumber = true,
  -- termguicolors = true,
}

vim.opt.shortmess:append "c"

for k, v in pairs(options) do
  vim.opt[k] = v
end
