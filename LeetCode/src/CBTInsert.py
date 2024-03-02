from TreeNode import TreeNode

class CBTInserter(object):
    """_summary_

    Args:
        object (_type_): _description_
    """

    """
    A complete binary tree is a binary tree in which every level, except possibly the last, is completely filled, and all nodes are as far left as possible.

    Design an algorithm to insert a new node to a complete binary tree keeping it complete after the insertion.

    Implement the CBTInserter class:

    - CBTInserter(TreeNode root) Initializes the data structure with the root of the complete binary tree.
    - int insert(int v) Inserts a TreeNode into the tree with value Node.val == val so that the tree remains complete, and returns the value of the parent of the inserted TreeNode.
    - TreeNode get_root() Returns the root node of the tree.
    
    Link:
    https://leetcode.com/problems/complete-binary-tree-inserter/description/
    """
    def __init__(self, root):
        """
        :type root: TreeNode
        """
        self.root = root
        self.current_level = [root]
        while (len(self.current_level)>0):
            next = []
            for n in self.current_level:
                if n.left != None:
                    next.append(n.left)
                else:
                    break

                if n.right != None:
                    next.append(n.right)
                else:
                    break
            if self.current_level[len(self.current_level)-1].right != None:
                self.current_level = next
            else:
                break
        idx = 0
        while self.current_level[idx].right != None:
            idx += 1
        self.add_children_index = idx

    def insert(self, val):
        """
        :type val: int
        :rtype: int
        """
        addChild = self.current_level[self.add_children_index]
        if addChild.left == None:
            addChild.left = TreeNode(val)
        else:
            addChild.right = TreeNode(val)
            self.add_children_index += 1
            if self.add_children_index >= len(self.current_level):
                next_level = []
                for node in self.current_level:
                    next_level.append(node.left)
                    next_level.append(node.right)
                self.current_level = next_level
                self.add_children_index = 0
        return addChild.val

    def get_root(self):
        """
        :rtype: TreeNode
        """
        return self.root

root = TreeNode.make_node([1, 2, 3, 4, 5, 6])
inserter = CBTInserter(root)
inserter.insert(7)
inserter.insert(8)
