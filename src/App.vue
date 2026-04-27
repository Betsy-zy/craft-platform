<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style>
:root {
  --primary-color: #8B0000;
  --secondary-color: #ffd04b;
  --background-color: #f5f5f5;
  --white: #ffffff;
  --border-color: #e0e0e0;
  --font-family: Arial, sans-serif;
  --font-size-large: 20px;
  --font-size-xlarge: 24px;
  --border-radius-sm: 4px;
  --border-radius-md: 8px;
  --spacing-sm: 10px;
  --spacing-md: 20px;
  --spacing-lg: 20px;
  --spacing-xl: 40px;
  --shadow-sm: 0 2px 4px rgba(0, 0, 0, 0.1);
  --shadow-md: 0 4px 8px rgba(0, 0, 0, 0.15);
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: var(--font-family);
  background-color: var(--background-color);
}

#app {
  width: 100%;
  min-height: 100vh;
}
</style>