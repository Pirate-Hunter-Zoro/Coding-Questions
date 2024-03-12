class tree_node(object):
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

    @staticmethod
    def make_node(val_list: list[int]):
        root = tree_node(val_list[0])
        idx = 1
        node_map = {}
        node_map[0] = root
        for i in range(1, len(val_list)):
            v = val_list[i]
            if v is not None:
                node_map[i] = tree_node(v)

        parent_index = 0
        while idx < len(val_list) and parent_index < len(val_list):
            parent = node_map[parent_index]
            v = val_list[idx]
            if v is not None:
                parent.left = node_map[idx]
            idx += 1
            if idx < len(val_list):
                v = val_list[idx]
                if v is not None:
                    parent.right = node_map[idx]
            idx += 1
            parent_index += 1
            while parent_index < len(val_list) and not parent_index in node_map.keys():
                parent_index += 1

        return root

    def __eq__(self, __value: object) -> bool:
        if not isinstance(__value, self.__class__):
            return False

        if self.val == __value.val:
            if self.left == None and self.right == None:
                return __value.left == None and __value.right == None
            elif self.left == None:
                return __value.left == None and self.right == __value.right
            elif self.right == None:
                return __value.right == None and self.left == __value.left
            else:
                return self.left == __value.left and self.right == __value.right
        else:
            return False
