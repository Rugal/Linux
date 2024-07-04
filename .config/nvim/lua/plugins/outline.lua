---@type NvPluginSpec[]
local plugins = {

  ------------------------------------
  -- code outline
  ------------------------------------
  -- F12
  -- K : toggle preview
  -- h : fold
  -- l : unfold
  -- E : unfold all
  -- W : fold all
  -- a : code action
  -- o : focus
  -- <CR> : goto
  {
    "hedyhli/outline.nvim",
    config = function()
      -- Example mapping to toggle outline
      require("outline").setup {
        -- Your setup opts here (leave empty to use defaults)
      }
    end,
  },
}

return plugins
